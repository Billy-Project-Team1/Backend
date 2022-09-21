package com.sparta.billy.socket.dto;

import com.sparta.billy.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDetailDto {
    private String nickname;
    private String profileUrl;
    private Boolean response;
    private String message;
    private Boolean chatOwner;

    public MemberDetailDto(Boolean response, String message, Member member, Boolean chatOwner) {
        this.response = response;
        this.message = message;
        this.nickname = member.getNickname();
        this.profileUrl = member.getProfileUrl();
        this.chatOwner = chatOwner;
    }
}