package com.sparta.billy.util;

import com.sparta.billy.exception.ex.*;
import com.sparta.billy.model.*;
import com.sparta.billy.repository.*;
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
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewImgUrlRepository reviewImgUrlRepository;
    private final TokenProvider tokenProvider;

    public void checkPost(Post post) {
        if (post == null) throw new NotFoundPostException();
    }

    public void checkReview(Review review) {
        if (review == null) throw new NotFoundReviewException();
    }

    public void checkReservation(Reservation reservation) { if (reservation == null) throw new NotFoundReservationException(); }

    public void checkPostAuthor(Member member, Post post) {
        if (!post.getMember().equals(member)) throw new NotAuthorException();
    }

    public void checkReviewAuthor(Member member, Review review) {
        if (!review.getMember().equals(member)) throw new NotAuthorException();
    }

    public void checkReservationPostAuthor(Member member, Reservation reservation) {
        if (!reservation.getJully().equals(member)) throw new IllegalArgumentException("게시글 작성자만 예약 상태를 변경할 수 있습니다.");
    }

    public void checkReservationAuthor(Member member, Reservation reservation) {
        if (!reservation.getBilly().equals(member)) throw new IllegalArgumentException("예약 당사자만 취소할 수 있습니다.");
    }

    public void validateReservation(Reservation reservation) {
        if (reservation.getState() == 3) throw new IllegalArgumentException("이미 취소된 예약건입니다.");
    }

    public void validateDelivery(int state, Reservation reservation) {
        if (state == 4) {
            if (!reservation.isDelivery()) throw new IllegalArgumentException("아직 빌리가 전달받지 않은 상태입니다.");
        }
    }

    public Post getCurrentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public Reservation getCurrentReservation(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation.orElse(null);
    }

    public List<PostImgUrl> getPostImgUrlByPost(Post post) {
        return postImgUrlRepository.findAllByPost(post);
    }

    public List<BlockDate> getBlockDateByPost(Post post) {
        return blockDateRepository.findAllByPost(post);
    }

    public List<Review> getReviewByPost(Post post) {
        return reviewRepository.findAllByPost(post);
    }

    public List<ReviewImgUrl> getReviewImgUrlByReview(Review review) { return reviewImgUrlRepository.findAllByReview(review); }
    public List<Reservation> getReservationByPost(Post post) { return reservationRepository.findAllByPost(post); }

    public Review getCurrentReview(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        return optionalReview.orElse(null);
    }

    public void tokenCheck(HttpServletRequest request, Member member) {
        if (request.getHeader("Authorization") == null) throw new TokenNotExistException();
        if (member == null) throw new MemberNotFoundException();
    }

    public Member getMemberByUserId(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public Member getMember() {
        return tokenProvider.getMemberFromAuthentication();
    }
}
