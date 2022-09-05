package com.sparta.billy.controller;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PutMapping("/auth/posts/{postId}/likes")
    public ResponseDto<?> Like(@PathVariable Long postId, HttpServletRequest request){
        return likeService.upDownLike(postId, request);
    }
}
