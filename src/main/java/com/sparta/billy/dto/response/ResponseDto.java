package com.sparta.billy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private T result;
    private boolean success;
    private Status status;


    public static <T> ResponseDto<T> success(T result) {
        return new ResponseDto<>(result,true, new Status("200", "정상적으로 처리되었습니다."));
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(new Status(code, message));
    }

    public ResponseDto(Status status) {
    }

    @Getter
    @AllArgsConstructor
    static class Status {
        private String code;
        private String message;
    }

}
