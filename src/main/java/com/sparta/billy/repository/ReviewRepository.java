package com.sparta.billy.repository;

import com.sparta.billy.model.Post;
import com.sparta.billy.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPost(Post post);

    void deleteByPost(Post post);
}
