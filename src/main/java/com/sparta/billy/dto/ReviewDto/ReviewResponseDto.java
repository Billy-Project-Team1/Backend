package com.sparta.billy.dto.ReviewDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;

    private String nickname;

    private String profileUrl;

    private Long authorId;

    private int star;

    private String comment;

    private String reviewImg;

    private boolean isMine;

    private List<ReviewChildrenResponseDto> children = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateAt;

    @QueryProjection
    public ReviewResponseDto(Long reviewId, String nickname, String profileUrl, Long authorId,
                             int star, String comment, String reviewImg,
                             LocalDateTime createAt, LocalDateTime updateAt, boolean isMine) {
        this.reviewId = reviewId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.authorId = authorId;
        this.star = star;
        this.comment = comment;
        this.reviewImg = reviewImg;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isMine = isMine;
    }

    @QueryProjection
    public ReviewResponseDto(Review review, boolean isMine) {
        this.reviewId = review.getId();
        this.nickname = review.getMember().getNickname();
        this.profileUrl = review.getMember().getProfileUrl();
        this.authorId = review.getMember().getId();
        this.star = review.getStar();
        this.comment = review.getComment();;
        this.reviewImg = review.getReviewImg();
        this.isMine = isMine;
        this.createAt = review.getCreatedAt();
        this.updateAt = review.getUpdatedAt();
    }

}
