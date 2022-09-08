package com.sparta.billy.dto.ReviewDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {
    private Long reservationId;

    private int star;

    private String comment;
}
