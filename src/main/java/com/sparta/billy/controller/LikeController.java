package com.sparta.billy.controller;

import com.sparta.billy.dto.PostDto.PostResponseDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PutMapping("/auth/posts/{postId}/likes")
    public ResponseDto<String> likeCreateOrDelete(@PathVariable Long postId, HttpServletRequest request){
        return likeService.upDownLike(postId, request);
    }

    @GetMapping("/auth/posts/likes")
    public ResponseDto<List<PostResponseDto>> likePostByMember(HttpServletRequest request) {
        return likeService.getLikePostListByMember(request);
    }
}
