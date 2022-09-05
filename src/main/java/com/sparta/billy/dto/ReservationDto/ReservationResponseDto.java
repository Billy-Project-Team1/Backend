package com.sparta.billy.dto.ReservationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private Long postId;
    private Long jullyId;
    private String jullyNickname;
    private String title;
    private int price;
    private int deposit;
    private String location;

    private Long billyId;
    private String billyNickname;
    private int state;
    private String startDate;
    private String endDate;
    // 대여 총 예상 금액
    private int totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @QueryProjection
    public ReservationResponseDto(Reservation reservation, Post post, int totalAmount) {
        this.reservationId = reservation.getId();
        this.postId = post.getId();
        this.jullyId = post.getMember().getId();
        this.jullyNickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.price = post.getPrice();
        this.deposit = post.getDeposit();
        this.location = post.getLocation();
        this.billyId = reservation.getBilly().getId();
        this.billyNickname = reservation.getBilly().getNickname();
        this.state = reservation.getState();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();;
        this.totalAmount = totalAmount;
        this.createdAt = reservation.getCreatedAt();
        this.updatedAt = reservation.getUpdatedAt();
    }

}
