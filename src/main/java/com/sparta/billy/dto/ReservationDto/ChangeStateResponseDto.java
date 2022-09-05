package com.sparta.billy.dto.ReservationDto;

import com.sparta.billy.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStateResponseDto {
    private Long reservationId;
    private String postOwner;
    private String reservationOwner;
    private String changePostTitle;
    private int state;
    private String cancelMessage;

    public ChangeStateResponseDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.postOwner = reservation.getPost().getMember().getNickname();
        this.reservationOwner = reservation.getBilly().getNickname();
        this.changePostTitle = reservation.getPost().getTitle();
        this.state = reservation.getState();
        this.cancelMessage = reservation.getCancelMessage();
    }
}
