package com.sparta.billy.service;

import com.sparta.billy.dto.MemberDto.MemberSignupRequestDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void createMember() {

    }


    @Test
    void emailDuplicateCheck() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void getMemberDetails() {
    }

    @Test
    void deleteMember() {
    }

    @Test
    void reissue() {
    }

    @Test
    void tokenToHeaders() {
    }

    private Member createMember(MemberSignupRequestDto requestDto) {
        Member member = new Member();
        return Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(requestDto.getPassword())
                .build();
    }


    private MemberSignupRequestDto createMemberRequest() {
        MemberSignupRequestDto requestDto = new MemberSignupRequestDto();
        requestDto.setEmail("kbss318@naver.com");
        requestDto.setNickname("bogeul");
        requestDto.setPassword("aaa1111!");
        return requestDto;
    }
}