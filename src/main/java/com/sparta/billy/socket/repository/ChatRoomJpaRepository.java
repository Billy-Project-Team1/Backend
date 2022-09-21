package com.sparta.billy.socket.repository;

import com.sparta.billy.model.Post;
import com.sparta.billy.socket.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
    boolean existsByRoomId(String roomId);
    Optional<ChatRoom> findByNicknameAndPost(String nickname, Post post);
}