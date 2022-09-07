package com.sparta.billy.socket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.billy.model.UserDetailsImpl;
import com.sparta.billy.socket.dto.ChatMessageDto;
import com.sparta.billy.socket.dto.MemberDetailDto;
import com.sparta.billy.socket.dto.MemberinfoDto;
import com.sparta.billy.socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    @ResponseBody
    public List<ChatMessageDto> getMessage(@PathVariable String roomId) {
        return chatService.getMessages(roomId);
    }


    //채팅방에 참여한 사용자 정보 조회
    @GetMapping("/chat/message/memberinfo/{roomId}")
    @ResponseBody
    public List<MemberinfoDto> getUserInfo(
            @PathVariable String roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.getUserinfo(userDetails, roomId);
    }

    //유저 정보 상세 조회 (채팅방 안에서)
    @GetMapping("/chat/details/{roomId}/{memberId}")
    @ResponseBody
    public ResponseEntity<MemberDetailDto> getUserDetails(@PathVariable String roomId, @PathVariable Long memberId) {
        return chatService.getUserDetails(roomId,memberId);
    }
}