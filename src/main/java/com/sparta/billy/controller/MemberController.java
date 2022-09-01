package com.sparta.billy.controller;

import com.sparta.billy.dto.request.LoginDto;
import com.sparta.billy.dto.request.MemberSignupRequestDto;
import com.sparta.billy.dto.response.ResponseDto;
import com.sparta.billy.dto.response.SuccessDto;
import com.sparta.billy.exception.ex.DuplicateEmailException;
import com.sparta.billy.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseEntity<SuccessDto> signup(@RequestBody MemberSignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    @GetMapping("/members/email-check")
    public ResponseEntity<SuccessDto> emailDuplicateCheck(String email) {
        return memberService.emailDuplicateCheck(email);
    }

    @PostMapping("/members/login")
    public ResponseDto<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        return memberService.login(loginDto, response);
    }

    @PostMapping("/members/reissue")
    public ResponseDto<?> reissue(String email, HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(email, request, response);
    }

    @PostMapping("/members/logout")
    public ResponseEntity<SuccessDto> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }
}
