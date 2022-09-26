package com.sparta.billy.service;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewImgUrlResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewRequestDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.exception.ex.ReservationException.NotFoundReservationException;
import com.sparta.billy.model.*;
import com.sparta.billy.repository.ReservationRepository;
import com.sparta.billy.repository.ReviewImgUrlRepository;
import com.sparta.billy.repository.ReviewQueryRepository;
import com.sparta.billy.repository.ReviewRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final AwsS3Service awsS3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewImgUrlRepository reviewImgUrlRepository;
    private final Check check;

    @Transactional
    public ResponseDto<?> createReview(ReviewRequestDto reviewRequestDto,
                                       List<MultipartFile> files,
                                       HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        check.tokenCheck(request, member);

        Reservation reservation = reservationRepository.findById(reviewRequestDto.getReservationId())
                .orElseThrow(NotFoundReservationException::new);

        Post post = reservation.getPost();
        check.checkPost(post);

        if (reservation.getState() != 5) {
            throw new IllegalArgumentException("반납완료된 예약건이 아닙니다.");
        }

        Optional<Review> optionalReview = reviewRepository.findByMemberAndReservation(member, reservation);

        if (optionalReview.isPresent()) {
            throw new IllegalArgumentException("이미 리뷰를 남긴 예약건입니다.");
        }

        boolean isMine = true;
        Review review = Review.builder()
                .member(member)
                .post(post)
                .reservation(reservation)
                .star(reviewRequestDto.getStar())
                .comment(reviewRequestDto.getComment())
                .build();
        reviewRepository.save(review);

        List<ReviewImgUrlResponseDto> imgList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile imgFile : files) {
                String fileName = awsS3Service.upload(imgFile);
                String imgUrl = URLDecoder.decode(fileName, "UTF-8");
                if (imgUrl.equals("false")) {
                    throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
                }
                ReviewImgUrl reviewImgUrl = ReviewImgUrl.builder()
                        .imgUrl(imgUrl)
                        .review(review)
                        .build();
                reviewImgUrlRepository.save(reviewImgUrl);
                ReviewImgUrlResponseDto imgUrlResponseDto = ReviewImgUrlResponseDto.builder()
                        .reviewId(review.getId())
                        .reviewImgUrl(imgUrl)
                        .build();
                imgList.add(imgUrlResponseDto);
            }
        }

        return ResponseDto.success(new ReviewResponseDto(review, imgList, isMine));
    }

    @Transactional
    public ResponseDto<?> updateReview(Long reviewId,
                                       ReviewRequestDto reviewRequestDto,
                                       List<MultipartFile> files,
                                       List<String> imgUrlList,
                                       HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        Review review = check.getCurrentReview(reviewId);
        check.checkReviewAuthor(member, review);
        check.tokenCheck(request, member);

        List<ReviewImgUrlResponseDto> imgList = new ArrayList<>();
        if (files != null) {
            reviewImgUrlRepository.deleteByReview(review);
            for (MultipartFile imgFile : files) {
                String fileName = awsS3Service.upload(imgFile);
                String imgUrl = URLDecoder.decode(fileName, "UTF-8");
                if (imgUrl.equals("false")) {
                    throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
                }
                ReviewImgUrl reviewImgUrl = ReviewImgUrl.builder()
                        .imgUrl(imgUrl)
                        .review(review)
                        .build();
                reviewImgUrlRepository.save(reviewImgUrl);
                ReviewImgUrlResponseDto imgUrlResponseDto = ReviewImgUrlResponseDto.builder()
                        .reviewId(review.getId())
                        .reviewImgUrl(imgUrl)
                        .build();
                imgList.add(imgUrlResponseDto);
            }
            if (imgUrlList != null) {
                for (String img : imgUrlList) {
                    ReviewImgUrlResponseDto imgUrlDto = ReviewImgUrlResponseDto.builder()
                            .reviewId(review.getId())
                            .reviewImgUrl(img)
                            .build();
                    imgList.add(imgUrlDto);
                }
            }
        }

        boolean isMine = true;
        review.update(reviewRequestDto);
        return ResponseDto.success(new ReviewResponseDto(review, imgList, isMine));
    }

    @Transactional
    public ResponseEntity<SuccessDto> deleteReview(Long reviewId, HttpServletRequest request) {
        Member member = check.validateMember(request);
        Review review = check.getCurrentReview(reviewId);
        check.checkReviewAuthor(member, review);
        check.tokenCheck(request, member);

        List<ReviewImgUrl> reviewImgUrls = check.getReviewImgUrlByReview(review);

        if (!reviewImgUrls.isEmpty()) {
            reviewImgUrlRepository.deleteByReview(review);
        }
        reviewRepository.delete(review);

        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional
    public ResponseDto<?> getReviewsByPost(Long postId, String userId) {
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);
        List<ReviewResponseDto> reviews = reviewQueryRepository.findReviewByPostId(postId, userId);
        return ResponseDto.success(reviews);
    }

    @Transactional
    public ResponseDto<?> getReceivedReview(String userId, HttpServletRequest request) {
        Member member = check.getMemberByUserId(userId);
        check.tokenCheck(request, member);

        List<ReviewResponseDto> response = reviewQueryRepository.findReviewReceived(member);
        return ResponseDto.success(response);
    }
}
