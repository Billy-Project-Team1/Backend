package com.sparta.billy.socket.dto;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.PostImgUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private String lastMessage;
    private String postUrl;
    private String postTitle;
    private String nickname;
    private String profileUrl;
    private String lastMessageTime;
    private Long postId;

}