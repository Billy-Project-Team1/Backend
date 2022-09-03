package com.sparta.billy.controller;

import com.sparta.billy.dto.MemberDto.LoginDto;
import com.sparta.billy.dto.MemberDto.MemberUpdateRequestDto;
import com.sparta.billy.dto.MemberDto.MemberSignupRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = {"회원관리 Controller"})
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

    @PutMapping("/members/profile/{memberId}")
    public ResponseDto<?> memberUpdate(@PathVariable Long memberId,
                                       @RequestPart(value = "data") MemberUpdateRequestDto memberRequestDto,
                                        @RequestPart(value = "image", required = false) MultipartFile file,
                                        HttpServletRequest request) throws IOException {
        return memberService.updateMember(memberId, memberRequestDto, file, request);
    }

    @GetMapping("/members/profile/{memberId}")
    public ResponseDto<?> memberDetails(@PathVariable Long memberId) {
        return memberService.getMemberDetails(memberId);
    }

    @DeleteMapping("/members/withdrawal/{memberId}")
    public ResponseEntity<SuccessDto> memberDelete(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }

    @PostMapping("/members/reissue")
    public ResponseDto<?> reissue(String userId, HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(userId, request, response);
    }

    @PostMapping("/members/logout")
    public ResponseEntity<SuccessDto> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }
}
