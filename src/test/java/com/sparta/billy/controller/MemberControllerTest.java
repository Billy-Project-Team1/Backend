package com.sparta.billy.controller;

import com.google.gson.Gson;
import com.sparta.billy.dto.MemberDto.LoginDto;
import com.sparta.billy.dto.MemberDto.MemberResponseDto;
import com.sparta.billy.dto.MemberDto.MemberSignupRequestDto;
import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.dto.SuccessDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles="USER")
    void memberCreate() throws Exception {
        MemberSignupRequestDto requestDto = new MemberSignupRequestDto();
        requestDto.setEmail("kbss318@naver.com");
        requestDto.setNickname("bogeul");
        requestDto.setPassword("aaa1111!");

        given(memberService.createMember(requestDto)).willReturn(
                ResponseEntity.ok().body(SuccessDto.valueOf("true"))
        );

        Gson gson = new Gson();
        String request = gson.toJson(requestDto);

        mockMvc.perform(
                post("/members/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).createMember(refEq(requestDto));
    }

    @Test
    @WithMockUser(roles="USER")
    void emailDuplicateCheck() throws Exception {
        given(memberService.emailDuplicateCheck("boseul@naver.com")).willReturn(
                ResponseEntity.ok().body(SuccessDto.valueOf("true"))
        );

        mockMvc.perform(
                        get("/members/email-check")
                                .param("email","boseul@naver.com")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).emailDuplicateCheck("boseul@naver.com");
    }

//    @Test
//    @WithMockUser(roles="USER")
//    void login() throws Exception {
//        LoginDto requestDto = new LoginDto();
//        requestDto.setEmail("boseul@naver.com");
//        requestDto.setPassword("aaa1111!");
//        HttpServletResponse response = null;
//
//        Member member = new Member();
//        member.setEmail("boseul@naver.com");
//        member.setNickname("bogeul");
//        member.setUserId("ab1c3dd3");
//        member.setPassword("aaa1111!");
//
//        given(memberService.login(requestDto, response)).willReturn(
//            ResponseDto.success(new MemberResponseDto(member))
//        );
//
//        Gson gson = new Gson();
//        String request = gson.toJson(requestDto);
//
//        mockMvc.perform(
//                        post("/members/login")
//                                .content(request)
//                                .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.memberId").exists())
//                .andExpect(jsonPath("$.memberNickname").exists())
//                .andExpect(jsonPath("$.memberEmail").exists())
//                .andExpect(jsonPath("$.member").exists())
//                .andDo(print());
//
//        verify(memberService).emailDuplicateCheck("boseul@naver.com");
//    }

    @Test
    void memberUpdate() {
    }

    @Test
    void memberDetails() {
    }

    @Test
    void memberDelete() {
    }

    @Test
    void reissue() {
    }

    @Test
    void logout() {
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