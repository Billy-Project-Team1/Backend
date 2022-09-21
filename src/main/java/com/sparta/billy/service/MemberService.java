package com.sparta.billy.service;

import com.sparta.billy.dto.MemberDto.*;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.exception.ex.MemberException.DuplicateEmailException;
import com.sparta.billy.exception.ex.MemberException.MemberNotFoundException;
import com.sparta.billy.exception.ex.MemberException.TokenExpiredException;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.RefreshToken;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.repository.RefreshTokenRepository;
import com.sparta.billy.repository.ReviewQueryRepository;
import com.sparta.billy.security.jwt.TokenProvider;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final AwsS3Service awsS3Service;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewQueryRepository reviewQueryRepository;
    private final TokenProvider tokenProvider;
    private final Check check;

    // 회원가입
    @Transactional
    public ResponseEntity<SuccessDto> createMember(MemberSignupRequestDto signupRequestDto) {
        String uuid = UUID.randomUUID().toString();
        String userId = uuid.substring(0, 8);
        Member member = Member.builder()
                .email(signupRequestDto.getEmail())
                .userId(userId)
                .nickname(signupRequestDto.getNickname())
                .profileUrl("https://billy-img-bucket.s3.ap-northeast-2.amazonaws.com/nullimg.png")
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
    public ResponseDto<MemberResponseDto> login(LoginDto loginDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberNotFoundException();
        }

        if (refreshTokenRepository.findByMember(member).isPresent()) {
            refreshTokenRepository.deleteByMember(member);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(new MemberResponseDto(member));
    }

    @Transactional
    public ResponseEntity<SuccessDto> logout(HttpServletRequest request) {
        Member member = check.validateMember(request);
        tokenProvider.deleteRefreshToken(member);
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional
    public ResponseDto<?> updateMember(String userId, MemberUpdateRequestDto memberRequestDto, MultipartFile file, HttpServletRequest request) throws IOException {
        Member checkMember = check.validateMember(request);
        check.tokenCheck(request, checkMember);

        if (!checkMember.getUserId().equals(userId)) {
            throw new IllegalArgumentException("자신의 프로필만 수정가능합니다.");
        }

        Member member = check.getMemberByUserId(userId);
        String profileUrl;
        if (file != null) {
            if (member.getProfileUrl() != null) {
                String key = member.getProfileUrl().substring("https://billy-img-bucket.s3.ap-northeast-2.amazonaws.com/".length());
                awsS3Service.deleteS3(key);
            }
            profileUrl = awsS3Service.upload(file);
            member.updateProfile(memberRequestDto, profileUrl);
        } else {
            member.updateProfile(memberRequestDto, null);
        }

        return ResponseDto.success(MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .userId(member.getUserId())
                .profileUrl(member.getProfileUrl())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build());
    }

    @Transactional
    public ResponseDto<?> getMemberDetails(String userId) {
        Member member = check.getMemberByUserId(userId);
        if (member == null) {
            throw new MemberNotFoundException();
        }

        String totalAvg = reviewQueryRepository.getTotalAvg(member);
        return ResponseDto.success(new MemberResponseDto(member.getId(), member.getEmail(), member.getNickname(),
                member.getUserId(), member.getProfileUrl(), totalAvg));
    }

    @Transactional
    public ResponseEntity<SuccessDto> deleteMember(String userId) {
        Member member = check.getMemberByUserId(userId);

        if (member == null) {
            throw new MemberNotFoundException();
        }

        if (refreshTokenRepository.findByMember(member).isPresent()) {
            refreshTokenRepository.deleteByMember(member);
        }
        memberRepository.delete(member);
        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }

    @Transactional
    public ResponseDto<?> reissue(RefreshTokenDto refreshTokenDto, HttpServletRequest request, HttpServletResponse response) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            throw new TokenExpiredException();
        }
        Member member = memberRepository.findByUserId(refreshTokenDto.getUserId()).orElse(null);
        if (null == member) {
            throw new MemberNotFoundException();
        }
        RefreshToken refreshToken = tokenProvider.presentRefreshToken(member);

        if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
            throw new TokenExpiredException();
        }

        TokenDto tokenDto = tokenProvider.generateAccessTokenDto(member);
        tokenToHeaders(tokenDto, response);
        return ResponseDto.success("success");
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}
