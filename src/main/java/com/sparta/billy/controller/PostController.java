package com.sparta.billy.controller;

import com.sparta.billy.dto.PostDto.PostUploadRequestDto;
import com.sparta.billy.dto.PostDto.SearchRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/auth/posts")
    public ResponseDto<?> postCreate(@RequestPart PostUploadRequestDto postUploadRequestDto,
                                     @RequestParam(required = false) List<String> blockDateDtoList,
                                     @RequestPart(required = false) List<MultipartFile> files,
                                     HttpServletRequest request) throws IOException {
        if (files == null) {
            throw new IllegalArgumentException("MULTIPART FILE IS EMPTY");
        }

        return postService.createPost(postUploadRequestDto, blockDateDtoList, files, request);
    }

    @PatchMapping("/auth/posts/{postId}")
    public ResponseDto<?> postUpdate(@PathVariable Long postId,
                                     @RequestPart(required = false) PostUploadRequestDto postUploadRequestDto,
                                     @RequestParam(required = false) List<String> blockDateDtoList,
                                     @RequestPart(required = false) List<MultipartFile> files,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(postId, postUploadRequestDto, blockDateDtoList, files, request);
    }

    @DeleteMapping("/auth/posts/{postId}")
    public ResponseEntity<SuccessDto> postDelete(@PathVariable Long postId, HttpServletRequest request) {
        return postService.deletePost(postId, request);
    }

    @GetMapping("/posts/details/{postId}")
    public ResponseDto<?> postDetails(@PathVariable Long postId, @RequestParam(required = false) String userId) {
        return postService.getPostDetails(postId, userId);
    }

    @GetMapping("/auth/posts/my-page")
    public ResponseDto<?> postMyUpload(HttpServletRequest request) {
        return postService.getMyPost(request);
    }

    @GetMapping("/posts")
    public ResponseDto<?> postAll(Long lastPostId, Pageable pageable) {
        return postService.getAllPosts(lastPostId, pageable);
    }

    @PostMapping("/posts/search")
    public ResponseDto<?> postBySearching(@RequestBody(required = false) SearchRequestDto searchRequestDto) {
        return postService.getPostsBySearching(searchRequestDto);
    }

    @GetMapping("/posts/elasticsearch")
    public ResponseDto<?> search(@RequestBody(required = false) SearchRequestDto searchRequestDto) throws IOException {
        return postService.getPostsByElasticSearch(searchRequestDto);
    }
}
