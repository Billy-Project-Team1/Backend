package com.sparta.billy.repository;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByPost(Post post);
    int countByPost(Post post);
    int countReservationByBillyAndState(Member billy, int state);
    int countReservationByJullyAndState(Member jully, int state);
    List<Reservation> findAllByBilly(Member billy);
    List<Reservation> findAllByJully(Member jully);
}
