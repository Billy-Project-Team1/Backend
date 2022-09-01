package com.sparta.billy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.billy.dto.KakaoMemberInfoDto;
import com.sparta.billy.dto.TokenDto;
import com.sparta.billy.dto.response.SuccessDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoMemberService {

    @Value("${kakao.client_id}")
    String kakaoClientId;
    @Value("${kakao.redirect_uri}")
    String RedirectURI;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public ResponseEntity<?> kakaoLogin(String code , HttpServletResponse response) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo(accessToken);

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member kakaoMember = registerKakaoMemberIfNeeded(kakaoMemberInfo);

        TokenDto tokenDto = tokenProvider.generateTokenDto(kakaoMember);
        memberService.tokenToHeaders(tokenDto, response);

        return ResponseEntity.ok().body(SuccessDto.valueOf("true"));
    }


    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", RedirectURI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        rt.setErrorHandler(new DefaultResponseErrorHandler(){
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }


        private KakaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoMemberInfoRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            rt.setErrorHandler(new DefaultResponseErrorHandler(){
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    HttpStatus statusCode = response.getStatusCode();
                    return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
                }
            });

            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoMemberInfoRequest,
                    String.class
            );

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            Long id = jsonNode.get("id").asLong();
            String nickname = jsonNode.get("properties")
                    .get("nickname").asText();
            String email = jsonNode.get("kakao_account")
                    .get("email").asText();
            String profileUrl = jsonNode.get("properties")
                    .get("profile_image").asText();

            return new KakaoMemberInfoDto(id, nickname, email, profileUrl);
        }

    private Member registerKakaoMemberIfNeeded(KakaoMemberInfoDto kakaoMemberInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoMemberInfo.getKakaoId();
        Member findKakaoMember = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (findKakaoMember == null) {
            // 회원가입
            // username: kakao nickname
            String nickname = kakaoMemberInfo.getNickname();

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            Member kakaoMember = new Member(nickname, encodedPassword, kakaoId);
            memberRepository.save(kakaoMember);

            return kakaoMember;
        }
        return findKakaoMember;
    }
}
