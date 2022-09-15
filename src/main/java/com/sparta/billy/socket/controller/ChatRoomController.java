package com.sparta.billy.socket.controller;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.UserDetailsImpl;
import com.sparta.billy.socket.dto.ChatListMessageDto;
import com.sparta.billy.socket.dto.ChatResponseDto;
import com.sparta.billy.socket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    // 내 채팅방 목록 반환
    @GetMapping("/chat/rooms")
    @ResponseBody
    public ChatListMessageDto room(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        return chatRoomRepository.findAllRoom(member);
    }

    // 특정 채팅방 입장
    @PostMapping("/chat/room/{postId}/{roomId}")
    @ResponseBody
    public ResponseDto<?> roomInfo(@PathVariable Long postId,@PathVariable String roomId) {

        String post = String.valueOf(postId);
        return  ResponseDto.success(new ChatResponseDto(roomId,post));
    }
}
