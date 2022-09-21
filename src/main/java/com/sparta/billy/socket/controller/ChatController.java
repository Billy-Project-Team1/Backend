package com.sparta.billy.socket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.billy.socket.dto.ChatMessageDto;
import com.sparta.billy.socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping({"/chat/message"})
    public void message(ChatMessageDto message, @Header("PK") Long pk) throws JsonProcessingException {
        chatService.save(message, pk);
    }

    //이전 채팅 기록 조회
    @GetMapping("/chat/message/{roomId}")
    public List<ChatMessageDto> getMessage(@PathVariable String roomId) {
        return chatService.getMessages(roomId);
    }
}