package com.sparta.billy.dto.ReviewDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {
    private Long reservationId;

    private int star;

    private String comment;
}
