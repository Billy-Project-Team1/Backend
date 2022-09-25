package com.sparta.billy.socket.dto;

import com.sparta.billy.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private String password;

    private String nickName;

    private String profileUrl;

    private Long kakaoId;

    public MemberDto(Member member) {
        this.password = member.getPassword();
        this.nickName = member.getNickname();
        this.profileUrl = member.getProfileUrl();
        this.kakaoId = member.getKakaoId();
    }
}