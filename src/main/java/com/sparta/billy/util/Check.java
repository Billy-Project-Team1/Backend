package com.sparta.billy.util;

import com.sparta.billy.exception.ex.*;
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

//    public void checkPost(Post post) {
//        if (post == null) throw new NotFoundPostException();
//    }
//
//    public void checkComment(Comment comment) {
//        if (comment == null) throw new NotFoundCommentException();
//    }

//    public void checkPostAuthor(Member member, Post post) {
//        if (!post.getMember().equals(member)) throw new NotAuthorException();
//    }
//
//    public void checkCommentAuthor(Member member, Comment comment) {
//        if (!comment.getMember().equals(member)) throw new NotAuthorException();
//    }

//    @Transactional(readOnly = true)
//    public Post getCurrentPost(Long id) {
//        Optional<Post> optionalPost = postRepository.findById(id);
//        return optionalPost.orElse(null);
//    }
//
//    @Transactional(readOnly = true)
//    public Comment getCurrentComment(Long id) {
//        Optional<Comment> optionalComment = commentRepository.findById(id);
//        return optionalComment.orElse(null);
//    }

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

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }
}
