package com.sparta.billy.service;

import com.sparta.billy.dto.PostDto.BlockDateResponseDto;
import com.sparta.billy.dto.PostDto.PostDetailResponseDto;
import com.sparta.billy.dto.PostDto.PostImgUrlResponseDto;
import com.sparta.billy.dto.PostDto.PostUploadRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.model.*;
import com.sparta.billy.repository.*;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ReservationRepository reservationRepository;
    private final Check check;

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
                .latitude(postUploadRequestDto.getLatitude())
                .longitude(postUploadRequestDto.getLongitude())
                .member(member)
                .build();
        postRepository.save(post);

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
        for (String date : blockDateDtoList) {
            BlockDate blockDate = BlockDate.builder()
                    .blockDate(date)
                    .post(post)
                    .build();
            blockDateRepository.save(blockDate);
            dateList.add(date);
            blockDateDto = new BlockDateResponseDto(dateList);
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
        if (!blockDateDtoList.isEmpty()) {
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

        // 게시글에 따른 평균 별점
        String postAvg = reviewQueryRepository.getPostAvg(post);
        if (postAvg.equals("NaN")) postAvg = "0";

        // 게시글에 달린 리뷰 수
        int reviewCount = reviewRepository.countByPost(post);

        // 내가 작성한 글인지 확인 (게시글 상세 조회는 로그인하지 않은 상태로도 조회가 가능해야하기 때문에 memberId를 따로 전달)
        boolean isMyPost = post.getMember().getUserId().equals(userId);

        return ResponseDto.success(new PostDetailResponseDto(post, blockDateResponseDto,
                postImgUrlResponseDto, isMyPost, likeCount, postAvg, reviewCount));
    }

    @Transactional
    public ResponseDto<?> getMyPost(HttpServletRequest request) {
        Member member = check.validateMember(request);
        return ResponseDto.success(postQueryRepository.findMyPost(member));
    }
}
