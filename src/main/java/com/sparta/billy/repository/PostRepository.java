package com.sparta.billy.repository;

import com.sparta.billy.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByMemberId(Long memberId);
}
