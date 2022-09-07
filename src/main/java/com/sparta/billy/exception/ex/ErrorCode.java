package com.sparta.billy.exception.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL("M001", "중복된 사용자 EMAIL이 존재합니다."),
    MEMBER_NOT_FOUND("M002", "존재하지 않은 사용자입니다."),
    PROFILE_NOT_FOUND("M003", "프로필 정보가 없습니다."),
    NOT_FOUND_POST("P001", "존재하지 않는 게시글입니다."),
    ALREADY_DELETE_POST("P002", "삭제된 게시글입니다."),
    NOT_FOUND_REVIEW("R001", "존재하지 않는 리뷰입니다."),
    NOT_FOUND_RESERVATION("R002", "존재하지 않는 예약건입니다."),
    DELIVERY_NOT_YET("R003", "전달완료가 되지 않은 상태입니다."),
    NOT_AUTHOR("A001", "작성자가 아닙니다."),
    TOKEN_EXPIRED("T001", "만료된 토큰입니다."),
    TOKEN_NOT_EXIST("T002", "토큰이 존재하지 않습니다.");

    private final String code;
    private final String message;

}