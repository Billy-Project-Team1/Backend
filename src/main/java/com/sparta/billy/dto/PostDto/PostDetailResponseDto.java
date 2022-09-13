package com.sparta.billy.dto.PostDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.billy.model.Post;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private int likeCount;
    private int reservationCount;
    private String postAvg;
    private int reviewCount;
    private String nickname;
    private String profileUrl;
    private String authorId;
    private String title;
    private String content;
    private int price;
    private int deposit;
    private BlockDateResponseDto blockDate;
    private PostImgUrlResponseDto postImgUrl;
    private String location;
    private String detailLocation;
    private Double latitude;
    private Double longitude;
    private boolean isMine;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @QueryProjection
    public PostDetailResponseDto(Post post,
                                 BlockDateResponseDto blockDate,
                                 PostImgUrlResponseDto postImgUrl,
                                 boolean isMine, int likeCount, String postAvg, int reviewCount, int reservationCount) {
        this.id = post.getId();
        this.likeCount = likeCount;
        this.reservationCount = reservationCount;
        this.postAvg = postAvg;
        this.reviewCount = reviewCount;
        this.nickname = post.getMember().getNickname();
        this.profileUrl = post.getMember().getProfileUrl();
        this.authorId = post.getMember().getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.deposit = post.getDeposit();
        this.blockDate = blockDate;
        this.postImgUrl = postImgUrl;
        this.location = post.getLocation();
        this.detailLocation = post.getDetailLocation();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.isMine = isMine;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public PostDetailResponseDto(Post post,
                                 BlockDateResponseDto blockDate,
                                 PostImgUrlResponseDto postImgUrl,
                                 boolean isMine) {
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.profileUrl = post.getMember().getProfileUrl();
        this.authorId = post.getMember().getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.deposit = post.getDeposit();
        this.blockDate = blockDate;
        this.postImgUrl = postImgUrl;
        this.location = post.getLocation();
        this.detailLocation = post.getDetailLocation();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.isMine = isMine;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
