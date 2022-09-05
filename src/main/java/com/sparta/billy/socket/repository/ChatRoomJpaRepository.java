package com.sparta.billy.socket.repository;

import com.sparta.billy.socket.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByNickname(String nickname);
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}