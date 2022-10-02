package com.sparta.billy.controller;

import com.sparta.billy.dto.MemberDto.LoginDto;
import com.sparta.billy.dto.MemberDto.MemberUpdateRequestDto;
import com.sparta.billy.dto.MemberDto.MemberSignupRequestDto;
import com.sparta.billy.dto.MemberDto.RefreshTokenDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseEntity<SuccessDto> memberCreate(@RequestBody MemberSignupRequestDto signupRequestDto) {
        return memberService.createMember(signupRequestDto);
    }

    @GetMapping("/members/email-check")
    public ResponseEntity<SuccessDto> emailDuplicateCheck(String email) {
        return memberService.emailDuplicateCheck(email);
    }

    @PostMapping("/members/login")
    public ResponseDto<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        return memberService.login(loginDto, response);
    }

    @PatchMapping("/auth/members/profile/{userId}")
    public ResponseDto<?> memberUpdate(@PathVariable String userId,
                                       @RequestPart(value = "data") MemberUpdateRequestDto memberRequestDto,
                                       @RequestPart(value = "image", required = false) MultipartFile file,
                                        HttpServletRequest request) throws IOException {
        return memberService.updateMember(userId, memberRequestDto, file, request);
    }

    @GetMapping("/auth/members/profile/{userId}")
    public ResponseDto<?> memberDetails(@PathVariable String userId, HttpServletRequest request) {
        return memberService.getMemberDetails(userId, request);
    }

    @DeleteMapping("/auth/members/withdrawal/{userId}")
    public ResponseEntity<SuccessDto> memberDelete(@PathVariable String userId, HttpServletRequest request) {
        return memberService.deleteMember(userId, request);
    }

    @PostMapping("/auth/members/reissue")
    public ResponseDto<?> reissue(@RequestBody RefreshTokenDto refreshTokenDto, HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(refreshTokenDto, request, response);
    }

    @PostMapping("/auth/members/logout")
    public ResponseEntity<SuccessDto> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }
}
