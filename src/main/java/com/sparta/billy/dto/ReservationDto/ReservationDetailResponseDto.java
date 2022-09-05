package com.sparta.billy.dto.ReservationDto;

import com.sparta.billy.dto.PostDto.PostImgUrlResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResponseDto {
    private Long postId;
    private Long jullyId;
    private String jullyNickname;
    private String title;
    private int price;
    private int deposit;
    private PostImgUrlResponseDto postImgUrl;
    private String location;
    private String latitude;
    private String longitude;
    private Long billyId;
    private String billyNickname;
    private int state;
    private String startDate;
    private String endDate;
    // 대여 총 예상 금액
    private int totalAmount;
}
