package com.sparta.billy.controller;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenRequestDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.ReviewChildrenService;
import com.sparta.billy.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ReviewChildrenController {
    private final ReviewChildrenService reviewChildrenService;
    private final ReviewService reviewService;

    @PostMapping("/auth/reviews/comments")
    public ResponseDto<?> commentCreate(@RequestBody ReviewChildrenRequestDto reviewChildrenRequestDto,
                                        HttpServletRequest request) {
        return reviewChildrenService.createComment(reviewChildrenRequestDto, request);
    }

    @PutMapping("/auth/reviews/comments/{commentId}")
    public ResponseDto<?> commentUpdate(@PathVariable Long commentId,
                                        @RequestBody ReviewChildrenRequestDto reviewChildrenRequestDto,
                                        HttpServletRequest request) {
        return reviewChildrenService.updateComment(commentId, reviewChildrenRequestDto, request);
    }

    @DeleteMapping("/auth/reviews/comments/{commentId}")
    public ResponseEntity<SuccessDto> commentDelete(@PathVariable Long commentId,
                                                    HttpServletRequest request) {
        return reviewService.deleteReview(commentId, request);
    }
}
