package com.sparta.billy.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.PostDto.BlockDateResponseDto;
import com.sparta.billy.dto.PostDto.PostImgUrlResponseDto;
import com.sparta.billy.dto.PostDto.PostResponseDto;
import com.sparta.billy.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.billy.model.QBlockDate.blockDate1;
import static com.sparta.billy.model.QLike.like;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QPostImgUrl.postImgUrl;
import static com.sparta.billy.model.QReservationDate.reservationDate1;
import static com.sparta.billy.model.QReview.review;
import static com.sparta.billy.model.QMember.member;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BlockDateResponseDto findBlockDateByPostId(Long postId) {
        List<String> dto = jpaQueryFactory.select(blockDate1.blockDate)
                .from(blockDate1)
                .where(blockDate1.post.id.eq(postId))
                .fetch();
        List<String> dto2 = jpaQueryFactory.select(reservationDate1.reservationDate)
                .from(reservationDate1)
                .where(reservationDate1.reservation.post.id.eq(postId))
                .fetch();
        return new BlockDateResponseDto(dto, dto2);
    }

    public PostImgUrlResponseDto findPostImgListByPostId(Long postId) {
        List<String> dto = jpaQueryFactory.select(postImgUrl.imgUrl)
                .from(postImgUrl)
                .where(postImgUrl.post.id.eq(postId))
                .fetch();

        return new PostImgUrlResponseDto(dto);
    }

    public List<PostResponseDto> findLikePostByMember(Member member1) {
        return jpaQueryFactory.select(Projections.constructor(PostResponseDto.class,
                        like.post.id, like.post.title, postImgUrl.imgUrl, like.post.location,
                        like.post.price, like.post.deposit,
                        JPAExpressions.select(review.star.avg())
                                .from(review)
                                .where(review.post.id.eq(like.post.id).and(review.parent.isNull())),
                        JPAExpressions.select(review.count())
                                .from(review)
                                .where(review.post.id.eq(like.post.id)),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(like)
                .innerJoin(like.post, post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(like.post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(like.post.id))))
                .where(like.member.id.eq(member1.getId()))
                .fetch();
    }

    public List<PostResponseDto> findMyPost(Member member1) {
        return jpaQueryFactory.select(Projections.constructor(PostResponseDto.class,
                        post.id, post.title, postImgUrl.imgUrl, post.location,
                        post.price, post.deposit,
                        JPAExpressions.select(review.star.avg())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(review.count())
                                .from(review)
                                .where(review.post.id.eq(post.id)),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(post.id))))
                .where(post.member.id.eq(member1.getId()))
                .fetch();
    }
}
