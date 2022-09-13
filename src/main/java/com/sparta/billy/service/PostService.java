package com.sparta.billy.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.sparta.billy.config.DocTestsTransport;
import com.sparta.billy.dto.PostDto.*;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
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
    public ResponseDto<?> createPost(PostUploadRequestDto postUploadRequestDto,
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
                .member(member)
                .build();
        postRepository.save(post);

        // elasticsearch document 저장
        PostDocument postDocument = new PostDocument(post.getId(), post.getTitle(),
                post.getLocation(), post.getTitle() + " " + post.getLocation());
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
    public ResponseDto<?> updatePost(Long postId,
                                     PostUploadRequestDto postUploadRequestDto,
                                     List<String> blockDateDtoList,
                                     List<MultipartFile> files,
                                     HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);
        check.checkPostAuthor(member, post);

        PostImgUrlResponseDto postImgUrlDto = null;
        if (files != null) {
            postImgUrlRepository.deleteByPost(post);
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
        }

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
        PostDocument postDocument = new PostDocument(post.getId(), post.getTitle(),
                post.getLocation(), post.getTitle() + " " + post.getLocation());
        postEsRepository.save(postDocument);
//        Map<String, Object> bodyMap = new HashMap<>();
//        bodyMap.put("title", post.getTitle());
//        bodyMap.put("location", post.getLocation());
//        bodyMap.put("titleAndLocation", post.getTitle() + " " + post.getLocation());
//        UpdateRequest updateRequest = new UpdateRequest("billy", String.valueOf(post.getId()));
//        updateRequest.doc(bodyMap);
        return ResponseDto.success(new PostDetailResponseDto(post, blockDateDto, postImgUrlDto, true));
    }

    @Transactional
    public ResponseEntity<SuccessDto> deletePost(Long postId, HttpServletRequest request) {
        Member member = check.validateMember(request);
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);
        check.checkPostAuthor(member, post);

        List<BlockDate> blockDates = check.getBlockDateByPost(post);
        List<PostImgUrl> postImgUrls = check.getPostImgUrlByPost(post);
        List<Review> reviews = check.getReviewByPost(post);
        List<Reservation> reservations = check.getReservationByPost(post);

        if (!blockDates.isEmpty()) {
            blockDateRepository.deleteByPost(post);
        }
        if (!postImgUrls.isEmpty()) {
            postImgUrlRepository.deleteByPost(post);
        }
        if (!reviews.isEmpty()) {
            reviewRepository.deleteByPost(post);
        }
        if (!reservations.isEmpty()) {
            for (Reservation r : reservations) {
                r.setPost(null);
            }
        }

        postRepository.delete(post);

        // elasticsearch document 삭제
        PostDocument postDocument = postEsRepository.findById(post.getId()).orElse(null);
        postEsRepository.delete(postDocument);
//        DeleteRequest deleteRequest = new DeleteRequest("billy", post.getId().toString());


        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    //게시글 상세 조회
    @Transactional
    public ResponseDto<?> getPostDetails(Long postId, String userId) {
        Post post = check.getCurrentPost(postId);
        PostImgUrlResponseDto postImgUrlResponseDto = postQueryRepository.findPostImgListByPostId(postId);
        BlockDateResponseDto blockDateResponseDto = postQueryRepository.findBlockDateByPostId(postId);

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

        return ResponseDto.success(new PostDetailResponseDto(post, blockDateResponseDto,
                postImgUrlResponseDto, isMyPost, likeCount, postAvg, reviewCount, reservationCount));
    }

    @Transactional
    public ResponseDto<?> getMyPost(HttpServletRequest request) {
        Member member = check.validateMember(request);
        return ResponseDto.success(postQueryRepository.findMyPost(member));
    }

    @Transactional
    public ResponseDto<?> getAllPosts(Long lastPostId, Pageable pageable) {
        Slice<PostResponseDto> response = postQueryRepository.findAllPostByPaging(lastPostId, pageable);
        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<?> getPostsBySearching(SearchRequestDto searchRequestDto) {
        List<PostResponseDto> response = postQueryRepository.findPostBySearching(searchRequestDto);
        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<?> getPostsByElasticSearch(SearchRequestDto searchRequestDto) throws IOException {
        SearchResponse<PostDocument> response = client.search(s -> s
                        .index("billy") // <1>
                        .query(q -> q      // <2>
                                .match(t -> t   // <3>
                                        .field("title")  // <4>
                                        .query(searchRequestDto.getKeyword())
//                                        .field("location")
//                                        .query(searchRequestDto.getKeyword())
                                )
                        ),
                PostDocument.class      // <5>
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            log.info("There are " + total.value() + " results");
        } else {
            log.info("There are more than " + total.value() + " results");
        }

        List<Long> postIdList = new ArrayList<>();
        List<Hit<PostDocument>> hits = response.hits().hits();
        for (Hit<PostDocument> hit: hits) {
            PostDocument postDocument = hit.source();
            log.info("Found product " + postDocument.getId() + ", score " + hit.score());
            postIdList.add(postDocument.getId());
        }
        //end::search-simple
        return ResponseDto.success(postIdList);
    }
}
