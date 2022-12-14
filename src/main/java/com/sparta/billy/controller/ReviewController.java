package com.sparta.billy.controller;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewRequestDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/auth/reviews")
    public ResponseDto<ReviewResponseDto> reviewCreate(@RequestPart ReviewRequestDto reviewRequestDto,
                                                       @RequestPart(required = false) List<MultipartFile> files,
                                                       HttpServletRequest request) throws IOException {
        return reviewService.createReview(reviewRequestDto, files, request);
    }

    @PatchMapping("/auth/reviews/{reviewId}")
    public ResponseDto<ReviewResponseDto> reviewUpdate(@PathVariable Long reviewId,
                                       @RequestPart ReviewRequestDto reviewRequestDto,
                                       @RequestPart(required = false) List<MultipartFile> files,
                                       @RequestParam(required = false) List<String> imgUrlList,
                                       HttpServletRequest request) throws IOException {
        return reviewService.updateReview(reviewId, reviewRequestDto, files, imgUrlList, request);
    }

    @DeleteMapping("/auth/reviews/{reviewId}")
    public ResponseEntity<SuccessDto> reviewDelete(@PathVariable Long reviewId, HttpServletRequest request) {
        return reviewService.deleteReview(reviewId, request);
    }

    @GetMapping("/reviews/{postId}")
    public ResponseDto<List<ReviewResponseDto>> reviewByPost(@PathVariable Long postId, @RequestParam(required = false) String userId) {
        return reviewService.getReviewsByPost(postId, userId);
    }

    @GetMapping("/auth/reviews/received/{userId}")
    public ResponseDto<List<ReviewResponseDto>> reviewByReceived(@PathVariable String userId) {
        return reviewService.getReceivedReview(userId);
    }
}
