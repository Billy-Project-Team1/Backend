package com.sparta.billy.repository;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Reservation;
import com.sparta.billy.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPost(Post post);
    Optional<Review> findByMemberAndReservation(Member member, Reservation reservation);

    int countByPost(Post post);
}
