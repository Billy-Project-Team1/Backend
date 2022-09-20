package com.sparta.billy.socket.repository;

import com.sparta.billy.socket.model.InvitedMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitedMembersRepository extends JpaRepository<InvitedMembers, Long> {

    void deleteByMemberIdAndRoomId(Long memberId, String roomId);
    boolean existsByMemberIdAndRoomId(Long member_id, String roomId);
    List<InvitedMembers> findAllByMemberId(Long memberId);
    List<InvitedMembers> findAllByRoomId(String roomId);

    List<InvitedMembers> findAllByMemberIdAndReadCheck(Long memberId, Boolean readCheck);

}
