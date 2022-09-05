package com.sparta.billy.exception;

public class MemberApiException extends RuntimeException{
    private static final String msg = "입력 정보를 다시 확인해 주세요.";
    public MemberApiException(){}
    public MemberApiException(String msg){super(msg);}
}
