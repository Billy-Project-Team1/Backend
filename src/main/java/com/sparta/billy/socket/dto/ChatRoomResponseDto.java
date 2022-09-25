package com.sparta.billy.socket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private String lastMessage;

    private String userId;

    private String otherNickname;

    private String profileUrl;

    private String lastMessageTime;

    private Long postId;

    private String postImgUrl;

    private String roomId;
}