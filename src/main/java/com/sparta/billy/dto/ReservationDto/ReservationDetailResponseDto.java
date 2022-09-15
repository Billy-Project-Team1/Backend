package com.sparta.billy.dto.ReservationDto;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
public class ReservationDetailResponseDto {
    private Long reservationId;
    private String jullyNickname;
    private String title;
    private String postImgUrl;
    private int price;
    private int deposit;
    private String startDate;
    private String endDate;
    private int totalAmount;// 총 대여 금액
    private String location;
    private String billyNickname;
    private boolean delivery;
    private int state;
    private String cancelMessage;

    public ReservationDetailResponseDto(Long reservationId, String jullyNickname, String title, String postImgUrl,
                                        int price, int deposit, String location,
                                        String billyNickname, int state, boolean delivery, String cancelMessage, String startDate, String endDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localStartDate = LocalDate.parse(startDate, dtf);
        LocalDate localEndDate = LocalDate.parse(endDate, dtf);

        this.reservationId = reservationId;
        this.jullyNickname = jullyNickname;
        this.title = title;
        this.postImgUrl = postImgUrl;
        this.totalAmount = (int)(ChronoUnit.DAYS.between(localStartDate, localEndDate)) * price;
        this.price = price;
        this.deposit = deposit;
        this.location = location;
        this.billyNickname = billyNickname;
        this.state = state;
        this.delivery = delivery;
        this.cancelMessage = cancelMessage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
