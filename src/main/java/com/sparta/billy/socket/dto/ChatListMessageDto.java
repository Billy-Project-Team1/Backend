package com.sparta.billy.socket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatListMessageDto {
    private List<ChatRoomResponseDto> chatRoomResponseDtoList;

    public ChatListMessageDto(List<ChatRoomResponseDto> chatRoomResponseDtoList) {
        this.chatRoomResponseDtoList = chatRoomResponseDtoList;
    }
}