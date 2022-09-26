package com.sparta.billy.socket.controller;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.socket.dto.ChatListMessageDto;
import com.sparta.billy.socket.dto.ChatResponseDto;
import com.sparta.billy.socket.repository.ChatRoomRepository;
import com.sparta.billy.socket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;


    // 내 채팅방 목록 반환
    @GetMapping("/chat/rooms")
    public ChatListMessageDto room(HttpServletRequest request) {
        return chatRoomRepository.findAllRoom(request);
    }

    // 특정 채팅방 입장
    @PostMapping("/chat/room/{postId}/{roomId}")
    public ResponseDto<?> roomInfo(@PathVariable Long postId, @PathVariable String roomId) {
        String post = String.valueOf(postId);
        return  ResponseDto.success(new ChatResponseDto(roomId, post));
    }

    //특정 채팅방 생성
    @PostMapping("/create/chat/{postId}")
    public ResponseDto<?> chatRoomCreate(@PathVariable Long postId, HttpServletRequest request) {
        return chatRoomService.createChatRoom(postId, request);
    }

}
