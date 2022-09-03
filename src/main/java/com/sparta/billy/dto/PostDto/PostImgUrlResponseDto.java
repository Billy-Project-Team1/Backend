package com.sparta.billy.dto.PostDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostImgUrlResponseDto {
    private List<String> postImgUrlList;

    public PostImgUrlResponseDto(List<String> postImgUrlList) {
        this.postImgUrlList = postImgUrlList;
    }
}
