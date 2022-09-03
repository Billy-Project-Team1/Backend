package com.sparta.billy.dto.PostDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BlockDateResponseDto {
    private List<String> blockDateList;

    public BlockDateResponseDto(List<String> blockDateList) {
        this.blockDateList = blockDateList;
    }
}
