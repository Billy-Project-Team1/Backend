package com.sparta.billy.dto.MemberDto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.billy.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
    private String userId;
    private String nickname;
    private String profileUrl;
    private String totalAvg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.profileUrl = member.getProfileUrl();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }

    public MemberResponseDto(Long id, String email, String nickname, String userId,
                             String profileUrl, String totalAvg) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userId = userId;
        this.profileUrl = profileUrl;
        this.totalAvg = totalAvg;
    }
}
