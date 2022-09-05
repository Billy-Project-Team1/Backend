package com.sparta.billy.dto;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeRequestDto {
    private Member member;
    private Post post;
}
