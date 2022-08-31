package com.sparta.billy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.billy.service.KakaoMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;



@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoMemberController {
    private final KakaoMemberService kakaoMemberService;

    @GetMapping("/member/kakaoLogin/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam(value = "code") String code,HttpServletResponse response) throws JsonProcessingException{
        return kakaoMemberService.kakaoLogin(code,response);
    }
}
