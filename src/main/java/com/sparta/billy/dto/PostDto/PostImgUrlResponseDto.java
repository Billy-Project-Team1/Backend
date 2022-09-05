package com.sparta.billy.dto.PostDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostImgUrlResponseDto {
    private List<String> postImgUrlList;

    @QueryProjection
    public PostImgUrlResponseDto(List<String> postImgUrlList) {
        this.postImgUrlList = postImgUrlList;
    }
}
