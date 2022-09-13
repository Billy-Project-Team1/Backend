package com.sparta.billy.dto.ReservationDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationCountDto {
    private int state1;
    private int state2;
    private int state3;
    private int state4;
    private int state5;

    public ReservationCountDto(int state1, int state2, int state3, int state4, int state5) {
        this.state1 = state1;
        this.state2 = state2;
        this.state3 = state3;
        this.state4 = state4;
        this.state5 = state5;
    }
}
