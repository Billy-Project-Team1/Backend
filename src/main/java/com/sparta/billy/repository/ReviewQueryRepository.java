package com.sparta.billy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.billy.model.QMember.member;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QReview.review;
import static com.sparta.billy.model.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ReviewResponseDto> findReviewByPostId(Long postId, String userId) {
        if (userId == null) {
            userId = null;
        }

        List<ReviewResponseDto> response = jpaQueryFactory.select(
                        Projections.constructor(ReviewResponseDto.class, review.id, review.member.nickname,
                                review.member.id, review.star, review.comment, review.reviewImg,
                                reservation.startDate, reservation.endDate,
                                review.createdAt, review.updatedAt, review.member.userId.eq(userId))
                )
                .from(review)
                .innerJoin(review.member, member)
                .innerJoin(review.reservation, reservation)
                .where(review.post.id.eq(postId), review.parent.id.isNull())
                .orderBy(review.createdAt.desc())
                .fetch();

        List<ReviewChildrenResponseDto> comments = jpaQueryFactory.select(
                        Projections.constructor(ReviewChildrenResponseDto.class, review.parent.id,
                                review.id, review.member.nickname,
                                review.member.id, review.comment, review.createdAt, review.updatedAt,
                                review.member.userId.eq(userId)
                        ))
                .from(review)
                .innerJoin(review.member, member)
                .where(review.parent.id.isNotNull())
                .fetch();

        response.stream()
                .forEach(parent -> {
                    parent.setChildren(comments.stream()
                            .filter(child -> child.getParentId().equals(parent.getReviewId()))
                            .collect(Collectors.toList()));
                });

        return response;
    }

    public List<ReviewResponseDto> findReviewReceived(Member member1) {
        List<ReviewResponseDto> response = jpaQueryFactory.select(
                        Projections.constructor(ReviewResponseDto.class, review.id, review.member.nickname,
                                review.member.id, review.star, review.comment, review.reviewImg,
                                reservation.startDate, reservation.endDate,
                                review.createdAt, review.updatedAt, review.member.id.eq(member1.getId()))
                )
                .from(review)
                .innerJoin(review.post, post)
                .innerJoin(review.member, member)
                .innerJoin(review.reservation, reservation)
                .where(review.parent.id.isNull(), reservation.jully.id.eq(member1.getId()))
                .orderBy(review.createdAt.desc())
                .fetch();

        List<ReviewChildrenResponseDto> comments = jpaQueryFactory.select(
                        Projections.constructor(ReviewChildrenResponseDto.class, review.parent.id,
                                review.id, review.member.nickname,
                                review.member.id, review.comment, review.createdAt, review.updatedAt,
                                review.member.id.eq(member1.getId())
                        ))
                .from(review)
                .innerJoin(review.post, post).innerJoin(review.member, member)
                .where(review.parent.id.isNotNull())
                .fetch();

        response.stream()
                .forEach(parent -> {
                    parent.setChildren(comments.stream()
                            .filter(child -> child.getParentId().equals(parent.getReviewId()))
                            .collect(Collectors.toList()));
                });
        return response;
    }

    public String getTotalAvg(Member member1) {
        List<Integer> reviewStar = jpaQueryFactory.select(review.star)
                .from(review)
                .where(review.parent.id.isNull(), review.reservation.jully.id.eq(member1.getId()))
                .fetch();
        int sum = 0;
        for (int i : reviewStar) {
            sum += i;
        }
        return String.format("%.1f" , (float)sum / (float)reviewStar.size());
    }

    public String getPostAvg(Post post1) {
        List<Integer> reviewStar = jpaQueryFactory.select(review.star)
                .from(review)
                .where(review.parent.id.isNull(), review.post.id.eq(post1.getId()))
                .fetch();
        int sum = 0;
        for (int i : reviewStar) {
            sum += i;
        }
        return String.format("%.1f" , (float)sum / (float)reviewStar.size());
    }

}
