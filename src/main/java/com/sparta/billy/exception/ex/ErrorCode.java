package com.sparta.billy.exception.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL("U002", "중복된 사용자 EMAIL이 존재합니다."),
    MEMBER_NOT_FOUND("U005", "존재하지 않은 사용자입니다."),
    PROFILE_NOT_FOUND("U006", "프로필 정보가 없습니다."),
    NOT_FOUND_POST("U007", "존재하지 않는 게시글입니다."),
    NOT_FOUND_COMMENT("U008", "존재하지 않는 댓글입니다."),
    NOT_AUTHOR("U009", "작성자가 아닙니다."),
    TOKEN_EXPIRED("U010", "만료된 토큰입니다.");

    private final String code;
    private final String message;

}
