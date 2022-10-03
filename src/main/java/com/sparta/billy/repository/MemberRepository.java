package com.sparta.billy.repository;

import com.sparta.billy.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserId(String userId);
    Optional<Member> findById(Long memberId);
    Long countByEmail(String email);
    void deleteById(Long memberId);
}
