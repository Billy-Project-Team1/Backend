package com.sparta.billy.dto;

import lombok.Getter;

@Getter
public class KakaoMemberInfoDto {
    private Long kakaoId;
    private String nickname;
    private String email;
    private String profileUrl;

    public KakaoMemberInfoDto(Long kakaoId, String nickname, String email, String profileUrl) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileUrl = profileUrl;
    }
}
