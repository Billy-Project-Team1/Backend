package com.sparta.billy.repository;

import com.sparta.billy.model.Reservation;
import com.sparta.billy.model.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Long> {
    void deleteAllByReservation(Reservation reservation);
}
