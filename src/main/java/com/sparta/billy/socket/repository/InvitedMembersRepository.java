package com.sparta.billy.socket.repository;

import com.sparta.billy.socket.model.InvitedMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitedMembersRepository extends JpaRepository<InvitedMembers, Long> {

    void deleteByMemberIdAndPostId(Long memberId, Long postId);
    boolean existsByMemberIdAndPostId(Long member_id, Long postId);
    List<InvitedMembers> findAllByMemberId(Long memberId);
    void deleteAllByPostId(Long postId);
    void deleteByMemberId(Long memberId);
    List<InvitedMembers> findAllByPostId(Long postId);
    InvitedMembers findByMemberIdAndPostId(Long id, Long id1);
    List<InvitedMembers> findAllByMemberIdAndReadCheck(Long memberId, Boolean readCheck);
    int countByPostId(Long postId);
    boolean existsByPostId(Long id);

}
