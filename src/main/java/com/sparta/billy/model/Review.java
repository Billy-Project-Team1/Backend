package com.sparta.billy.model;

import com.sparta.billy.dto.PostDto.PostUploadRequestDto;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenRequestDto;
import com.sparta.billy.dto.ReviewDto.ReviewRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int star;

    @Column
    private String comment;

    @Column
    private String reviewImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Review parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @JoinColumn(name = "reservation_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    public void update(ReviewRequestDto requestDto, String reviewImg) {
        if (requestDto.getStar() != 0) {
            this.star = requestDto.getStar();
        }
        if (requestDto.getComment() != null) {
            this.comment = requestDto.getComment();
        }
        if (reviewImg != null) {
            this.reviewImg = reviewImg;
        }
    }

    public void updateComment(ReviewChildrenRequestDto requestDto) {
        if (requestDto.getComment() != null) {
            this.comment = requestDto.getComment();
        }
    }
}
