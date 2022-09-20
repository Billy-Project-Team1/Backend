package com.sparta.billy.dto.PostDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BlockDateResponseDto {
    private List<String> blockDateList;
    private List<String> reservationDateList;

    public BlockDateResponseDto(List<String> blockDateList) {
        this.blockDateList = blockDateList;
    }

    public BlockDateResponseDto(List<String> blockDateList, List<String> reservationDateList) {
        this.blockDateList = blockDateList;
        this.reservationDateList = reservationDateList;
    }
}
