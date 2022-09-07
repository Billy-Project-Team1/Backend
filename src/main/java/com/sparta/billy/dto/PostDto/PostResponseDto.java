package com.sparta.billy.dto.PostDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String imgUrl;
    private String location;
    private int price;
    private int deposit;
    private String avg;
    private Long reviewCount;
    private Long likeCount;

    @QueryProjection
    public PostResponseDto(Long id, String title, String imgUrl,
                           String location, int price, int deposit,
                           Double num, Long reviewCount, Long likeCount) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.location = location;
        this.price = price;
        this.deposit = deposit;
        if (num == null) {
            this.avg = "0";
        } else {
            this.avg = String.format("%.1f", num);
        }
        this.reviewCount = reviewCount;
        this.likeCount = likeCount;
    }

    @QueryProjection
    public PostResponseDto(Long id, String title, String imgUrl,
                           String location, int price, int deposit) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.location = location;
        this.price = price;
        this.deposit = deposit;
    }
}
