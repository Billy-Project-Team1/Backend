package com.sparta.billy.repository;

import com.sparta.billy.model.Like;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndPost(Member member, Post post);
    int countByPost(Post post);
}
