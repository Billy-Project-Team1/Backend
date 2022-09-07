package com.sparta.billy.dto.PostDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String avg;
    private int reviewCount;
    private int likeCount;
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
    private boolean isMine;
    private List<ReviewResponseDto> reviews = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @QueryProjection
    public PostDetailResponseDto(Post post,
                                 BlockDateResponseDto blockDate,
                                 PostImgUrlResponseDto postImgUrl,
                                 List<ReviewResponseDto> reviews,
                                 boolean isMine, String avg, int likeCount) {
        this.id = post.getId();
        this.avg = avg;
        this.likeCount = likeCount;
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
        this.isMine = isMine;
        this.reviews = reviews;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public PostDetailResponseDto(Post post,
                                 BlockDateResponseDto blockDate,
                                 PostImgUrlResponseDto postImgUrl,
                                 List<ReviewResponseDto> reviews,
                                 boolean isMine) {
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
        this.isMine = isMine;
        this.reviews = reviews;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
