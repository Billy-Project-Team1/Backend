package com.sparta.billy.dto.ReviewDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class ReviewImgUrlResponseDto {
    private Long reviewId;
    private String reviewImgUrl;

    @QueryProjection
    public ReviewImgUrlResponseDto(Long reviewId, String reviewImgUrl) {
        this.reviewId = reviewId;
        this.reviewImgUrl = reviewImgUrl;
    }
}
