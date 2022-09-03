package com.sparta.billy.controller;

import com.sparta.billy.dto.PostDto.PostUploadRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseDto<?> postCreate(@RequestPart PostUploadRequestDto postUploadRequestDto,
                                     @RequestParam List<String> blockDateDtoList,
                                     @RequestPart(required = false) List<MultipartFile> files,
                                     HttpServletRequest request) throws IOException {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("MULTIPART FILE IS EMPTY");
        }
        return postService.createPost(postUploadRequestDto, blockDateDtoList, files, request);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseDto<?> postUpdate(@PathVariable Long postId,
                                     @RequestPart PostUploadRequestDto postUploadRequestDto,
                                     @RequestParam(required=false) List<String> blockDateDtoList,
                                     @RequestPart(required = false) List<MultipartFile> files,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(postId, postUploadRequestDto, blockDateDtoList, files, request);
    }

    @GetMapping("/posts/details/{postId}")
    public ResponseDto<?> postDetails(@PathVariable Long postId) {
        return postService.getPostsDetails(postId);
    }
}
