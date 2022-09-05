package com.sparta.billy.socket.repository;

import com.sparta.billy.socket.model.ResignChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResignChatMessageJpaRepository extends JpaRepository<ResignChatMessage, Long> {
}
