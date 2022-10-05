package com.sparta.billy.service;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenRequestDto;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenResponseDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Review;
import com.sparta.billy.repository.ReviewRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ReviewChildrenService {
    private final ReviewRepository reviewRepository;
    private final Check check;

    @Transactional
    public ResponseDto<ReviewChildrenResponseDto> createComment(ReviewChildrenRequestDto reviewChildrenRequestDto,
                                       HttpServletRequest request) {
        Member member = check.validateMember(request);
        Review review = check.getCurrentReview(reviewChildrenRequestDto.getReviewId());
        Post post = check.getCurrentPost(review.getPost().getId());
        check.checkReview(review);
        check.tokenCheck(request, member);

        boolean isMine = true;
        Review comment = Review.builder()
                .member(member)
                .post(post)
                .parent(review)
                .comment(reviewChildrenRequestDto.getComment())
                .build();
        reviewRepository.save(comment);
        return ResponseDto.success(new ReviewChildrenResponseDto(comment, isMine));
    }

    @Transactional
    public ResponseDto<ReviewChildrenResponseDto> updateComment(Long commentId,
                                       ReviewChildrenRequestDto reviewChildrenRequestDto,
                                       HttpServletRequest request) {
        Member member = check.validateMember(request);
        Review comment = check.getCurrentReview(commentId);
        check.checkReviewAuthor(member, comment);
        check.tokenCheck(request, member);

        boolean isMine = true;
        comment.updateComment(reviewChildrenRequestDto);
        return ResponseDto.success(new ReviewChildrenResponseDto(comment, isMine));
    }

}
