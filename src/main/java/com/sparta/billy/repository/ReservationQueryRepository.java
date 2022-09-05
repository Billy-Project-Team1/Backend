package com.sparta.billy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.ReservationDto.ReservationDetailResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.billy.model.QReservation.reservation;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QMember.member;
import static com.sparta.billy.model.QPostImgUrl.postImgUrl;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ReservationDetailResponseDto> findReservationByBillyAndState(Member billy, int state) {
        return jpaQueryFactory.select(
                        Projections.constructor(ReservationDetailResponseDto.class,
                        reservation.id, reservation.jully.nickname, reservation.post.title, postImgUrl.imgUrl, reservation.post.price,
                                reservation.post.deposit, reservation.post.location, reservation.billy.nickname,
                                reservation.state, reservation.startDate, reservation.endDate
                        )
                )
                .from(reservation)
                .innerJoin(reservation.post, post).on(post.id.eq(reservation.post.id))
                .innerJoin(reservation.billy, member).on(member.id.eq(reservation.billy.id))
                .leftJoin(postImgUrl).on(postImgUrl.post.id.eq(reservation.post.id)
                        .and(postImgUrl.post.id.eq(JPAExpressions.select(postImgUrl.post.id.min())
                        .from(postImgUrl))))
                .where(reservation.billy.id.eq(billy.getId()), reservation.state.eq(state))
                .orderBy(reservation.createdAt.desc())
                .fetch();
    }
}
