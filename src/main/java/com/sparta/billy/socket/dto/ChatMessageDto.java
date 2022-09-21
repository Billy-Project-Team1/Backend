package com.sparta.billy.socket.dto;

import com.sparta.billy.socket.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private ChatMessage.MessageType type; // 메시지 타입

    private String roomId; // 방번호

    private String message; // 메시지

    private String sender; // nickname

    private String profileUrl;

    private Long memberId;

    private String createdAt;

    public ChatMessageDto(ChatMessage chatMessage, String createdAt) {
        this.type = chatMessage.getType();
        this.roomId = chatMessage.getRoomId();
        this.message = chatMessage.getMessage();
        this.sender = chatMessage.getSender();
        this.profileUrl = chatMessage.getProfileUrl();
        this.createdAt = createdAt;
        this.memberId = chatMessage.getMemberId();
    }
}