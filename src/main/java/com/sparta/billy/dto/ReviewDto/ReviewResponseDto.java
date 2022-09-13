package com.sparta.billy.dto.ReviewDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.billy.model.Review;
import com.sparta.billy.model.ReviewImgUrl;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;

    private String nickname;

    private String authorId;

    private int star;

    private String comment;

    private String startDate;

    private String endDate;

    private int dateCount;

    private boolean isMine;

    private List<ReviewImgUrlResponseDto> reviewImgUrl;

    private List<ReviewChildrenResponseDto> children = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateAt;

    @QueryProjection
    public ReviewResponseDto(Long reviewId, String nickname, String authorId,
                             int star, String comment, String startDate, String endDate,
                             LocalDateTime createAt, LocalDateTime updateAt, boolean isMine) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localStartDate = LocalDate.parse(startDate, dtf);
        LocalDate localEndDate = LocalDate.parse(endDate, dtf);

        this.reviewId = reviewId;
        this.nickname = nickname;
        this.authorId = authorId;
        this.star = star;
        this.comment = comment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCount = (int)(ChronoUnit.DAYS.between(localStartDate, localEndDate));
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isMine = isMine;
    }

    @QueryProjection
    public ReviewResponseDto(Review review, List<ReviewImgUrlResponseDto> reviewImgUrl, boolean isMine) {
        this.reviewId = review.getId();
        this.nickname = review.getMember().getNickname();
        this.authorId = review.getMember().getUserId();
        this.star = review.getStar();
        this.comment = review.getComment();;
        this.reviewImgUrl = reviewImgUrl;
        this.isMine = isMine;
        this.createAt = review.getCreatedAt();
        this.updateAt = review.getUpdatedAt();
    }

}
