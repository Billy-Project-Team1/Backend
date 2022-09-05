package com.sparta.billy.dto.PostDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BlockDateResponseDto {
    private List<String> blockDateList;

    @QueryProjection
    public BlockDateResponseDto(List<String> blockDateList) {
        this.blockDateList = blockDateList;
    }
}
