package com.sparta.billy.repository;

import com.sparta.billy.model.Review;
import com.sparta.billy.model.ReviewImgUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgUrlRepository extends JpaRepository<ReviewImgUrl, Long> {
    void deleteByReview(Review review);

    List<ReviewImgUrl> findAllByReview(Review review);
}
