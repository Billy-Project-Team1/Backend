package com.sparta.billy.dto.ReviewDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewChildrenResponseDto {
    private Long parentId;

    private Long commentId;

    private String comment;

    private String nickname;

    private String profileUrl;

    private Long authorId;

    private boolean isMine;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateAt;

    @QueryProjection
    public ReviewChildrenResponseDto(Review review) {
        this.parentId = review.getParent().getId();
        this.commentId = review.getId();
        this.nickname = review.getMember().getNickname();
        this.profileUrl = review.getMember().getProfileUrl();
        this.authorId = review.getMember().getId();
        this.comment = review.getComment();;
        this.createAt = review.getCreatedAt();
        this.updateAt = review.getUpdatedAt();
    }

    @QueryProjection
    public ReviewChildrenResponseDto(Review review, boolean isMine) {
        this.parentId = review.getParent().getId();
        this.commentId = review.getId();
        this.nickname = review.getMember().getNickname();
        this.profileUrl = review.getMember().getProfileUrl();
        this.authorId = review.getMember().getId();
        this.comment = review.getComment();;
        this.isMine = isMine;
        this.createAt = review.getCreatedAt();
        this.updateAt = review.getUpdatedAt();
    }

    @QueryProjection
    public ReviewChildrenResponseDto(Long parentId, Long commentId, String nickname, String profileUrl,
                                     Long authorId, String comment,
                                     LocalDateTime createAt, LocalDateTime updateAt, boolean isMine) {
        this.parentId = parentId;
        this.commentId = commentId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.authorId = authorId;
        this.comment = comment;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isMine = isMine;
    }
}
