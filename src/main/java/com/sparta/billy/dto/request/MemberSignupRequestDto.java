package com.sparta.billy.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignupRequestDto {
    private String email;
    private String nickname;
    private String username;
    private String password;
}
