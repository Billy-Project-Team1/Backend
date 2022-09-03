package com.sparta.billy.dto.PostDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.billy.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String nickname;
    private String profileUrl;
    private Long authorId;
    private String title;
    private String content;
    private int price;
    private int deposit;
    private BlockDateResponseDto blockDate;
    private PostImgUrlResponseDto postImgUrl;
    private String location;
    private Double latitude;
    private Double longitude;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public PostDetailResponseDto(Post post, BlockDateResponseDto blockDate, PostImgUrlResponseDto postImgUrl) {
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.profileUrl = post.getMember().getProfileUrl();
        this.authorId = post.getMember().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.deposit = post.getDeposit();
        this.blockDate = blockDate;
        this.postImgUrl = postImgUrl;
        this.location = post.getLocation();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

}
