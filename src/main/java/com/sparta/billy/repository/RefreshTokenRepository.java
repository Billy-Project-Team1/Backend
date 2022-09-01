package com.sparta.billy.repository;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
  Member findByValue(String refreshToken);
}
