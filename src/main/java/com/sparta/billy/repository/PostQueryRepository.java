package com.sparta.billy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.PostDto.BlockDateResponseDto;
import com.sparta.billy.dto.PostDto.PostImgUrlResponseDto;
import com.sparta.billy.dto.PostDto.PostResponseDto;
import com.sparta.billy.dto.PostDto.SearchRequestDto;
import com.sparta.billy.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.billy.model.QBlockDate.blockDate1;
import static com.sparta.billy.model.QLike.like;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QPostImgUrl.postImgUrl;
import static com.sparta.billy.model.QReservationDate.reservationDate1;
import static com.sparta.billy.model.QReview.review;

@Slf4j
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
                                .where(review.post.id.eq(like.post.id).and(review.parent.isNull())),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(like)
                .innerJoin(like.post, post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(like.post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(like.post.id))))
                .where(like.member.id.eq(member1.getId()), like.post.isDelete.isFalse())
                .fetch();
    }

    public List<PostResponseDto> findMemberPost(Member member1) {
        return jpaQueryFactory.select(Projections.constructor(PostResponseDto.class,
                        post.id, post.title, postImgUrl.imgUrl, post.location,
                        post.price, post.deposit,
                        JPAExpressions.select(review.star.avg())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(review.count())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(post.id))))
                .where(post.member.id.eq(member1.getId()), post.isDelete.isFalse())
                .fetch();
    }

    public Slice<PostResponseDto> findAllPostByPaging(Long lastPostId, Pageable pageable) {
        List<PostResponseDto> results = jpaQueryFactory.select(Projections.constructor(PostResponseDto.class,
                        post.id, post.title, postImgUrl.imgUrl, post.location,
                        post.price, post.deposit,
                        JPAExpressions.select(review.star.avg())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(review.count())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(post.id))))
                .where(ltPostId(lastPostId), post.isDelete.isFalse())
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        return checkLastPage(pageable, results);
    }

    public List<PostResponseDto> findPostBySearching(SearchRequestDto searchRequestDto) {
        String[] keyword = searchRequestDto.getKeyword().split("\\s");
        return jpaQueryFactory.select(Projections.constructor(PostResponseDto.class,
                        post.id, post.title, postImgUrl.imgUrl, post.location,
                        post.price, post.deposit,
                        JPAExpressions.select(review.star.avg())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(review.count())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(post.id))))
                .where(post.location.contains(keyword[0]).and(post.title.contains(keyword[1])), post.isDelete.isFalse())
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    public PostResponseDto findPostByElasticSearch(Long postId) {
        return jpaQueryFactory.select(Projections.constructor(PostResponseDto.class,
                        post.id, post.title, postImgUrl.imgUrl, post.location,
                        post.price, post.deposit,
                        JPAExpressions.select(review.star.avg())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(review.count())
                                .from(review)
                                .where(review.post.id.eq(post.id).and(review.parent.isNull())),
                        JPAExpressions.select(like.id.count())
                                .from(like)
                                .where(like.post.id.eq(post.id))))
                .from(post)
                .leftJoin(postImgUrl)
                .on(postImgUrl.post.id.eq(post.id), postImgUrl.id.eq(JPAExpressions.select(postImgUrl.id.min())
                        .from(postImgUrl).where(postImgUrl.post.id.eq(post.id))))
                .where(post.id.eq(postId))
                .orderBy(post.createdAt.desc())
                .fetchOne();
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltPostId(Long storeId) {
        if (storeId == null) {
            return null;
        }
        return post.id.lt(storeId);
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<PostResponseDto> checkLastPage(Pageable pageable, List<PostResponseDto> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
