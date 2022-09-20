package com.sparta.billy.model;

import com.sparta.billy.dto.MemberDto.MemberUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private Long kakaoId;

    @Column
    private String profileUrl;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Builder
    public Member(String email, String userId, String password, String profileUrl, String nickname, Long kakaoId) {
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
    }

    public void updateProfile(MemberUpdateRequestDto memberRequestDto, String profileUrl) {
        if (memberRequestDto.getNickname() != null) {
            this.nickname = memberRequestDto.getNickname();
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
    }
}
