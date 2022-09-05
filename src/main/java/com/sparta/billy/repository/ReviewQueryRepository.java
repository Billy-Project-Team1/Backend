package com.sparta.billy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.billy.model.QMember.member;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ReviewResponseDto> findReviewByPostId(Long postId, Long memberId) {
        if (memberId == null) {
            memberId = 0L;
        }
        List<ReviewResponseDto> response = jpaQueryFactory.select(
                        Projections.constructor(ReviewResponseDto.class,review.id, review.member.nickname, review.member.profileUrl,
                                review.member.id, review.star, review.comment, review.reviewImg,
                                review.createdAt, review.updatedAt, review.member.id.eq(memberId))
                )
                .from(review)
                .innerJoin(review.post, post)
                .innerJoin(review.member, member)
                .where(post.id.eq(postId).and(review.parent.id.isNull()))
                .orderBy(review.createdAt.desc())
                .fetch();

        List<ReviewChildrenResponseDto> comments = jpaQueryFactory.select(
                        Projections.constructor(ReviewChildrenResponseDto.class, review.parent.id,
                                review.id, review.member.nickname, review.member.profileUrl,
                                review.member.id, review.comment, review.createdAt, review.updatedAt,
                                review.member.id.eq(memberId)
                        ))
                .from(review)
                .innerJoin(review.post, post).innerJoin(review.member, member)
                .where(post.id.eq(postId).and(review.parent.id.isNotNull()))
                .fetch();

        response.stream()
                .forEach(parent -> {
                    parent.setChildren(comments.stream()
                            .filter(child -> child.getParentId().equals(parent.getReviewId()))
                            .collect(Collectors.toList()));
                });
        return response;
    }
}
