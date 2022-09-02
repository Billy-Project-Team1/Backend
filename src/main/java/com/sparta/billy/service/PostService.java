package com.sparta.billy.service;

import com.sparta.billy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private PostRepository postRepository;
}
