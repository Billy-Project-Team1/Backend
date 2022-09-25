package com.sparta.billy.socket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberinfoDto {
    private String nickname;

    private String profileUrl;

    private Long userId;

    public MemberinfoDto(String nickname, String profileUrl, Long userId) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.userId = userId;
    }
}
