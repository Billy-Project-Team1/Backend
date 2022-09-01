package com.sparta.billy.util;

import com.sparta.billy.exception.ex.MemberNotFoundException;
import com.sparta.billy.exception.ex.TokenExpiredException;
import com.sparta.billy.exception.ex.TokenNotExistException;
import com.sparta.billy.model.Member;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Check {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public void tokenCheck(HttpServletRequest request, Member member) {
        if (request.getHeader("Authorization") == null) throw new TokenNotExistException();
        if (member == null) throw new MemberNotFoundException();
    }

    @Transactional(readOnly = true)
    public Member getCurrentMember(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
