package com.sparta.billy.socket.repository;

import com.sparta.billy.socket.model.ResignChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResignChatRoomJpaRepository extends JpaRepository<ResignChatRoom, Long> {
    ResignChatRoom findByRoomId(String roomId);
}
