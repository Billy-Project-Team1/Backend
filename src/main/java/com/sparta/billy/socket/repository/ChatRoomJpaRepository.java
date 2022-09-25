package com.sparta.billy.socket.repository;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.socket.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
    Optional<ChatRoom> findByMemberAndPost(Member member, Post post);

    List<ChatRoom> findAllByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);

}