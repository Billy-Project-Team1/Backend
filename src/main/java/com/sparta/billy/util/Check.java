package com.sparta.billy.util;

import com.sparta.billy.exception.ex.MemberNotFoundException;
import com.sparta.billy.exception.ex.NotAuthorException;
import com.sparta.billy.exception.ex.NotFoundPostException;
import com.sparta.billy.exception.ex.TokenNotExistException;
import com.sparta.billy.model.BlockDate;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.PostImgUrl;
import com.sparta.billy.repository.BlockDateRepository;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.repository.PostImgUrlRepository;
import com.sparta.billy.repository.PostRepository;
import com.sparta.billy.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Check {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostImgUrlRepository postImgUrlRepository;
    private final BlockDateRepository blockDateRepository;
    private final TokenProvider tokenProvider;

    public void checkPost(Post post) {
        if (post == null) throw new NotFoundPostException();
    }
//
//    public void checkComment(Comment comment) {
//        if (comment == null) throw new NotFoundCommentException();
//    }

    public void checkPostAuthor(Member member, Post post) {
        if (!post.getMember().equals(member)) throw new NotAuthorException();
    }
//
//    public void checkCommentAuthor(Member member, Comment comment) {
//        if (!comment.getMember().equals(member)) throw new NotAuthorException();
//    }

    @Transactional(readOnly = true)
    public Post getCurrentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PostImgUrl> getCurrentPostImgUrl(Post post) {
        return postImgUrlRepository.findAllByPost(post);
    }

    @Transactional(readOnly = true)
    public List<BlockDate> getCurrentBlockDate(Post post) {
        return blockDateRepository.findAllByPost(post);
    }
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
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Member getMemberByUserId(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.orElse(null);
    }
}
