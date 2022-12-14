package com.sparta.billy.socket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDto {
    private String roomId;

    private String postId;

    public ChatResponseDto(String roomId, String postId) {
        this.roomId = roomId;
        this.postId = postId;
    }
}
