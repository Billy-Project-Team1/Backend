package com.sparta.billy.dto.MemberDto;

import lombok.Getter;

@Getter
public class KakaoMemberInfoDto {
    private final String nickname;
    private final String email;
    private final String profileUrl;
    private final Long id;


    public KakaoMemberInfoDto(String nickname, String email, String profileUrl, Long id){
        this.nickname=nickname;
        this.email=email;
        this.profileUrl=profileUrl;
        this.id=id;

    }
}
