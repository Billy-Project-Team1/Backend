package com.sparta.billy.dto.PostDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUploadRequestDto {
    private String title;
    private String content;
    private int price;
    private int deposit;
    private String location;
    private Double latitude;
    private Double longitude;
}
