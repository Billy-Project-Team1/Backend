package com.sparta.billy.dto.ReservationDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationStateRequestDto {
    private int state;
    private String cancelMessage;
}
