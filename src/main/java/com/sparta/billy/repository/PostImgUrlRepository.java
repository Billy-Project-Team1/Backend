package com.sparta.billy.repository;

import com.sparta.billy.model.Post;
import com.sparta.billy.model.PostImgUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImgUrlRepository extends JpaRepository<PostImgUrl, Long> {
    List<PostImgUrl> findAllByPost(Post post);
    void deleteByPost(Post post);
}
