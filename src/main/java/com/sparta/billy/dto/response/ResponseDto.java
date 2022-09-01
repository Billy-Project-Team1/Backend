package com.sparta.billy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private T result;
    private boolean success;

    public static <T> ResponseDto<T> success(T result) {
        return new ResponseDto<>(result,true);
    }

}
