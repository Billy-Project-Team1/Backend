package com.sparta.billy.service;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewRequestDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Reservation;
import com.sparta.billy.model.Review;
import com.sparta.billy.repository.ReservationRepository;
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
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final AwsS3Service awsS3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final ReservationRepository reservationRepository;
    private final Check check;

    @Transactional
    public ResponseDto<?> createReview(ReviewRequestDto reviewRequestDto,
                                       MultipartFile file,
                                       HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        Post post = check.getCurrentPost(reviewRequestDto.getPostId());
        check.checkPost(post);
        check.tokenCheck(request, member);

        List<Reservation> reservations = reservationRepository.findAllByPost(post);
        for (Reservation r : reservations) {
            if (!r.getBilly().equals(member)) {
                if (r.getState() != 5) {
                    throw new IllegalArgumentException("반납 완료한 예약자만 작성가능합니다.");
                }
            }
        }

        String imgUrl;
        if (file != null) {
            String fileName = awsS3Service.upload(file);
            imgUrl = URLDecoder.decode(fileName, "UTF-8");
            if (imgUrl.equals("false")) {
                throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
            }
        } else {
            imgUrl = null;
        }
        boolean isMine = true;
        Review review = Review.builder()
                .member(member)
                .post(post)
                .star(reviewRequestDto.getStar())
                .reviewImg(imgUrl)
                .comment(reviewRequestDto.getComment())
                .build();
        reviewRepository.save(review);
        return ResponseDto.success(new ReviewResponseDto(review, isMine));
    }

    @Transactional
    public ResponseDto<?> updateReview(Long reviewId,
                                       ReviewRequestDto reviewRequestDto,
                                       MultipartFile file,
                                       HttpServletRequest request) throws IOException {
        Member member = check.validateMember(request);
        Review review = check.getCurrentReview(reviewId);
        check.checkReviewAuthor(member, review);
        check.tokenCheck(request, member);

        String imgUrl;
        if (file != null) {
            String fileName = awsS3Service.upload(file);
            imgUrl = URLDecoder.decode(fileName, "UTF-8");
            if (imgUrl.equals("false")) {
                throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
            }
        } else {
            imgUrl = null;
        }
        boolean isMine = true;
        review.update(reviewRequestDto, imgUrl);
        return ResponseDto.success(new ReviewResponseDto(review, isMine));
    }

    @Transactional
    public ResponseEntity<SuccessDto> deleteReview(Long reviewId, HttpServletRequest request) {
        Member member = check.validateMember(request);
        Review review = check.getCurrentReview(reviewId);
        check.checkReviewAuthor(member, review);
        check.tokenCheck(request, member);

        reviewRepository.delete(review);

        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional
    public ResponseDto<?> getReviewsByPost(Long postId, Long memberId) {
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);
        List<ReviewResponseDto> reviews = reviewQueryRepository.findReviewByPostId(postId, memberId);
        return ResponseDto.success(reviews);
    }

    @Transactional
    public ResponseDto<?> getReceivedReview(HttpServletRequest request) {
        Member member = check.validateMember(request);
        check.tokenCheck(request, member);

        List<ReviewResponseDto> response = reviewQueryRepository.findReviewReceived(member);
        return ResponseDto.success(response);
    }
}
