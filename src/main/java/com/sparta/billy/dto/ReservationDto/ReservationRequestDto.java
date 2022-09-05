package com.sparta.billy.dto.ReservationDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
    private Long postId;
    private String startDate;
    private String endDate;
}
