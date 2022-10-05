package com.sparta.billy.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.sparta.billy.config.DocTestsTransport;
import com.sparta.billy.dto.PostDto.*;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.exception.ex.PostException.DeletePostException;
import com.sparta.billy.exception.ex.PostException.NotFoundPostException;
import com.sparta.billy.model.*;
import com.sparta.billy.repository.*;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final AwsS3Service awsS3Service;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostImgUrlRepository postImgUrlRepository;
    private final BlockDateRepository blockDateRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final LikeRepository likeRepository;
    private final PostEsRepository postEsRepository;
    private final ReservationRepository reservationRepository;
    private final Check check;

    private final DocTestsTransport transport = new DocTestsTransport();
    private final ElasticsearchClient client = new ElasticsearchClient(transport);


    // 게시글 작성
    @Transactional
    public ResponseDto<PostDetailResponseDto> createPost(PostUploadRequestDto postUploadRequestDto,
                                     List<String> blockDateDtoList,
                                     List<MultipartFile> files,
                                     HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        check.tokenCheck(request, member);
        Post post = Post.builder()
                .title(postUploadRequestDto.getTitle())
                .content(postUploadRequestDto.getContent())
                .price(postUploadRequestDto.getPrice())
                .deposit(postUploadRequestDto.getDeposit())
                .location(postUploadRequestDto.getLocation())
                .detailLocation(postUploadRequestDto.getDetailLocation())
                .latitude(postUploadRequestDto.getLatitude())
                .longitude(postUploadRequestDto.getLongitude())
                .isDelete(false)
                .member(member)
                .build();
        postRepository.save(post);

        // elasticsearch document 저장
        PostDocument postDocument = new PostDocument(post.getId(), post.getTitle(), post.getDetailLocation(),
                post.getTitle() + " " + post.getDetailLocation());
        postEsRepository.save(postDocument);

        PostImgUrlResponseDto postImgUrlDto = null;
        List<String> imgList = new ArrayList<>();
        for (MultipartFile imgFile : files) {
            String fileName = awsS3Service.upload(imgFile);
            String imgUrl = URLDecoder.decode(fileName, "UTF-8");
            if (imgUrl.equals("false")) {
                throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
            }
            PostImgUrl postImgUrl = PostImgUrl.builder()
                    .imgUrl(imgUrl)
                    .post(post)
                    .build();
            postImgUrlRepository.save(postImgUrl);
            imgList.add(imgUrl);
            postImgUrlDto = new PostImgUrlResponseDto(imgList);
        }

        BlockDateResponseDto blockDateDto = null;
        List<String> dateList = new ArrayList<>();
        if (blockDateDtoList != null) {
            for (String date : blockDateDtoList) {
                BlockDate blockDate = BlockDate.builder()
                        .blockDate(date)
                        .post(post)
                        .build();
                blockDateRepository.save(blockDate);
                dateList.add(date);
                blockDateDto = new BlockDateResponseDto(dateList);
            }
        }
        return ResponseDto.success(new PostDetailResponseDto(post, blockDateDto, postImgUrlDto, true));
    }

    // 게시글 수정
    @Transactional
    public ResponseDto<PostDetailResponseDto> updatePost(Long postId,
                                     PostUploadRequestDto postUploadRequestDto,
                                     List<String> blockDateDtoList,
                                     List<MultipartFile> files,
                                     List<String> imgUrlList,
                                     HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);
        check.checkPostAuthor(member, post);

        PostImgUrlResponseDto postImgUrlDto = null;
        List<String> imgList = new ArrayList<>();
        postImgUrlRepository.deleteByPost(post);
        if (files != null) {
            for (MultipartFile imgFile : files) {
                String fileName = awsS3Service.upload(imgFile);
                String imgUrl = URLDecoder.decode(fileName, "UTF-8");
                if (imgUrl.equals("false")) {
                    throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
                }
                PostImgUrl postImgUrl = PostImgUrl.builder()
                        .imgUrl(imgUrl)
                        .post(post)
                        .build();
                postImgUrlRepository.save(postImgUrl);
                imgList.add(imgUrl);
            }
        }
        if (imgUrlList != null) {
            for (String img : imgUrlList) {
                PostImgUrl postImgUrl = PostImgUrl.builder()
                        .imgUrl(img)
                        .post(post)
                        .build();
                postImgUrlRepository.save(postImgUrl);
                imgList.add(img);
            }
        }
        if (files == null && imgUrlList == null) {
            throw new IllegalArgumentException("사진을 넣어주세요.");
        }
        postImgUrlDto = new PostImgUrlResponseDto(imgList);

        BlockDateResponseDto blockDateDto = null;
        if (blockDateDtoList != null) {
            blockDateRepository.deleteByPost(post);
            List<String> dateList = new ArrayList<>();
            for (String date : blockDateDtoList) {
                BlockDate blockDate = BlockDate.builder()
                        .blockDate(date)
                        .post(post)
                        .build();
                blockDateRepository.save(blockDate);
                dateList.add(date);
                blockDateDto = new BlockDateResponseDto(dateList);
            }
        }
        post.update(postUploadRequestDto);

        // elasticsearch document 수정
        PostDocument postDocument = new PostDocument(post.getId(), post.getTitle(), post.getDetailLocation(),
                post.getTitle() + " " + post.getDetailLocation());
        postEsRepository.save(postDocument);

        return ResponseDto.success(new PostDetailResponseDto(post, blockDateDto, postImgUrlDto, true));
    }

    @Transactional
    public ResponseEntity<SuccessDto> deletePost(Long postId, HttpServletRequest request) {
        Member member = check.validateMember(request);
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);
        check.checkPostAuthor(member, post);

        post.setDelete(true);

        // elasticsearch document 삭제
        PostDocument postDocument = postEsRepository.findById(post.getId()).orElse(null);
        postEsRepository.delete(postDocument);

        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    //게시글 상세 조회
    @Transactional
    public ResponseDto<PostDetailResponseDto> getPostDetails(Long postId, String userId) {
        Post post = check.getCurrentPost(postId);
        PostImgUrlResponseDto postImgUrlResponseDto = postQueryRepository.findPostImgListByPostId(postId);
        BlockDateResponseDto blockDateResponseDto = postQueryRepository.findBlockDateByPostId(postId);

        if (post.isDelete()) {
            throw new DeletePostException();
        }
        // 관심 수
        int likeCount = likeRepository.countByPost(post);

        // 대여 수
        int reservationCount = reservationRepository.countByPost(post);

        // 게시글에 따른 평균 별점
        String postAvg = reviewQueryRepository.getPostAvg(post);
        if (postAvg.equals("NaN")) postAvg = "0";

        // 게시글에 달린 리뷰 수
        int reviewCount = reviewRepository.countByPost(post);

        // 내가 작성한 글인지 확인 (게시글 상세 조회는 로그인하지 않은 상태로도 조회가 가능해야하기 때문에 memberId를 따로 전달)
        boolean isMyPost = post.getMember().getUserId().equals(userId);

        boolean isLike = likeRepository.findByMemberUserIdAndPost(userId, post).isPresent();

        return ResponseDto.success(new PostDetailResponseDto(post, blockDateResponseDto,
                postImgUrlResponseDto, isMyPost, likeCount, postAvg, reviewCount, reservationCount, isLike));
    }


    @Transactional
    public ResponseDto<List<PostResponseDto>> getMemberPost(String userId) {
        Member member = check.getMemberByUserId(userId);
        List<PostResponseDto> response = postQueryRepository.findMemberPost(member);
        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<Slice<PostResponseDto>> getAllPosts(Long lastPostId, Pageable pageable) {
        Slice<PostResponseDto> response = postQueryRepository.findAllPostByPaging(lastPostId, pageable);
        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<List<PostResponseDto>> getPostsByElasticSearch(SearchRequestDto searchRequestDto) {
        List<PostDocument> postDocumentList = postEsRepository.findBySearchKeyword(searchRequestDto.getKeyword());

        List<PostResponseDto> response = new ArrayList<>();

        for (PostDocument post : postDocumentList) {
            response.add(postQueryRepository.findPostByElasticSearch(post.getId()));
        }
        return ResponseDto.success(response);
    }
}
