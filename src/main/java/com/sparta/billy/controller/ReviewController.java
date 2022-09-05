package com.sparta.billy.controller;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewRequestDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/auth/reviews")
    public ResponseDto<?> reviewCreate(@RequestPart ReviewRequestDto reviewRequestDto,
                                       @RequestPart(required = false) MultipartFile file,
                                       HttpServletRequest request) throws IOException {
        return reviewService.createReview(reviewRequestDto, file, request);
    }

    @PatchMapping("/auth/reviews/{reviewId}")
    public ResponseDto<?> reviewUpdate(@PathVariable Long reviewId,
                                       @RequestPart ReviewRequestDto reviewRequestDto,
                                       @RequestPart(required = false) MultipartFile file,
                                       HttpServletRequest request) throws IOException {
        return reviewService.updateReview(reviewId, reviewRequestDto, file, request);
    }

    @DeleteMapping("/auth/reviews/{reviewId}")
    public ResponseEntity<SuccessDto> reviewDelete(@PathVariable Long reviewId, HttpServletRequest request) {
        return reviewService.deleteReview(reviewId, request);
    }

    @GetMapping("/reviews/{postId}")
    public ResponseDto<?> reviewByPost(@PathVariable Long postId, @RequestParam(required = false) Long memberId) {
        return reviewService.getReviewsByPost(postId, memberId);
    }
}