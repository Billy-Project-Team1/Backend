package com.sparta.billy.dto.ReviewDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewChildrenRequestDto {
    private Long reviewId;
    private String comment;
}
