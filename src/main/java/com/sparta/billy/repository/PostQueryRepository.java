package com.sparta.billy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.PostDto.BlockDateResponseDto;
import com.sparta.billy.dto.PostDto.PostImgUrlResponseDto;
import com.sparta.billy.dto.ReviewDto.QReviewChildrenResponseDto;
import com.sparta.billy.dto.ReviewDto.QReviewResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewChildrenResponseDto;
import com.sparta.billy.dto.ReviewDto.ReviewResponseDto;
import com.sparta.billy.model.Member;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.billy.model.QBlockDate.blockDate1;
import static com.sparta.billy.model.QMember.member;
import static com.sparta.billy.model.QPost.post;
import static com.sparta.billy.model.QPostImgUrl.postImgUrl;
import static com.sparta.billy.model.QReview.review;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BlockDateResponseDto findBlockDateByPostId(Long postId) {
        List<String> dto = jpaQueryFactory.select(blockDate1.blockDate)
                .from(blockDate1)
                .where(blockDate1.post.id.eq(postId))
                .fetch();

        return new BlockDateResponseDto(dto);
    }

    public PostImgUrlResponseDto findPostImgListByPostId(Long postId) {
        List<String> dto = jpaQueryFactory.select(postImgUrl.imgUrl)
                .from(postImgUrl)
                .where(postImgUrl.post.id.eq(postId))
                .fetch();

        return new PostImgUrlResponseDto(dto);
    }

}
