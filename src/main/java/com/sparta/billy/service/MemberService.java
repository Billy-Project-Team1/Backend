package com.sparta.billy.service;

import com.sparta.billy.dto.TokenDto;
import com.sparta.billy.dto.request.LoginDto;
import com.sparta.billy.dto.request.MemberSignupRequestDto;
import com.sparta.billy.dto.response.MemberResponseDto;
import com.sparta.billy.dto.response.ResponseDto;
import com.sparta.billy.dto.response.SuccessDto;
import com.sparta.billy.exception.ex.*;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.RefreshToken;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.repository.RefreshTokenRepository;
import com.sparta.billy.security.jwt.TokenProvider;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final Check check;

    // 회원가입
    @Transactional
    public ResponseEntity<SuccessDto> signup(MemberSignupRequestDto signupRequestDto) {
        Member member = Member.builder()
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<SuccessDto> emailDuplicateCheck(String email) {
        if (memberRepository.countByEmail(email) != 0) {
            throw new DuplicateEmailException();
        }
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional
    public ResponseDto<?> login(LoginDto loginDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberNotFoundException();
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(new MemberResponseDto(member));
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    @Transactional
    public ResponseEntity<SuccessDto> logout(HttpServletRequest request) {
        Member member = check.validateMember(request);
        tokenProvider.deleteRefreshToken(member);
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // RefreshToken 유효성 검사
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            throw new RuntimeException("Refresh-Token 기간이 만료 되었습니다.");
        }

        // 유저 정보 꺼내기
        Member member = refreshTokenRepository.findByValue(refreshToken);
        if (member == null) {
            throw new RuntimeException("토큰 정보가 없습니다.");
        }

        RefreshToken refreshTokenConfirm = refreshTokenRepository.findByMember(member).orElseThrow();

        if (refreshToken.equals(refreshTokenConfirm.getValue())) {
            // 토큰 재발행
            TokenDto tokenDto = tokenProvider.generateAccessTokenDto(member);
            accessTokenToHeaders(tokenDto, response);
            return new ResponseEntity<>(ResponseDto.success("ACCESS_TOKEN_REISSUE"), HttpStatus.OK);
        }
        // 기존 토큰 삭제
        tokenProvider.deleteRefreshToken(memberRepository.findById(member.getId()).get());
        throw new MemberNotFoundException();

    }
    public void accessTokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
