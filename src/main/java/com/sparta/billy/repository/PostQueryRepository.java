package com.sparta.billy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.billy.dto.PostDto.BlockDateResponseDto;
import com.sparta.billy.dto.PostDto.PostImgUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.billy.model.QBlockDate.blockDate1;
import static com.sparta.billy.model.QPostImgUrl.postImgUrl;

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
