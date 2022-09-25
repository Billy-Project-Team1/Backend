package com.sparta.billy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.ReservationDto.ReservationDetailResponseDto;
import com.sparta.billy.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.billy.model.QMember.member;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QPostImgUrl.postImgUrl;
import static com.sparta.billy.model.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ReservationDetailResponseDto> findReservationByBillyAndState(Member billy, int state) {
        return jpaQueryFactory.select(
                        Projections.constructor(ReservationDetailResponseDto.class,
                        reservation.id, reservation.jully.nickname, reservation.post.title, postImgUrl.imgUrl, reservation.post.price,
                                reservation.post.deposit, reservation.post.location, reservation.billy.nickname,
                                reservation.state, reservation.delivery, reservation.cancelMessage,
                                reservation.startDate, reservation.endDate, reservation.post.id
                        )
                )
                .from(reservation)
                .leftJoin(reservation.post, post)
                .on(post.id.eq(reservation.post.id).or(reservation.post.id.isNull()))
                .innerJoin(reservation.billy, member)
                .on(member.id.eq(billy.getId()))
                .innerJoin(reservation.jully, member)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(reservation.post.id)
                        .and(postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                                .from(postImgUrl).where(postImgUrl.post.id.eq(reservation.post.id)))))
                .where(reservation.state.eq(state))
                .orderBy(reservation.createdAt.desc())
                .fetch();
    }

    public List<ReservationDetailResponseDto> findReservationByJullyAndState(Member jully, int state) {
        return jpaQueryFactory.select(
                        Projections.constructor(ReservationDetailResponseDto.class,
                                reservation.id, reservation.jully.nickname, reservation.post.title, postImgUrl.imgUrl, reservation.post.price,
                                reservation.post.deposit, reservation.post.location, reservation.billy.nickname,
                                reservation.state, reservation.delivery, reservation.cancelMessage,
                                reservation.startDate, reservation.endDate, reservation.post.id
                        )
                )
                .from(reservation)
                .leftJoin(reservation.post, post)
                .on(post.id.eq(reservation.post.id).or(reservation.post.id.isNull()))
                .innerJoin(reservation.jully, member)
                .on(member.id.eq(jully.getId()))
                .innerJoin(reservation.billy, member)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(reservation.post.id)
                        .and(postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                                .from(postImgUrl).where(postImgUrl.post.id.eq(reservation.post.id)))))
                .where(reservation.state.eq(state))
                .orderBy(reservation.createdAt.desc())
                .fetch();
    }

}
