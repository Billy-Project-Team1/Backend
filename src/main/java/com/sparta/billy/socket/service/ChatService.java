package com.sparta.billy.socket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.billy.model.Member;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.repository.PostRepository;
import com.sparta.billy.socket.dto.ChatMessageDto;
import com.sparta.billy.socket.model.ChatMessage;
import com.sparta.billy.socket.model.InvitedMembers;
import com.sparta.billy.socket.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final PostRepository postRepository;
    private final SimpMessageSendingOperations sendingOperations;

    @Transactional
    public void save(ChatMessageDto messageDto, Long pk) throws JsonProcessingException {
        // 토큰에서 유저 아이디 가져오기
        Member member = memberRepository.findById(pk).orElseThrow(
                () -> new NullPointerException("존재하지 않는 사용자 입니다!")
        );
        LocalDateTime createdAt = LocalDateTime.now();
        String formatDate = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
        messageDto.setSender(member.getNickname());
        messageDto.setProfileUrl(member.getProfileUrl());
        messageDto.setCreatedAt(formatDate);
        messageDto.setMemberId(member.getId());

        //받아온 메세지의 타입이 ENTER 일때
        if (ChatMessage.MessageType.ENTER.equals(messageDto.getType())) {
            String roomId = messageDto.getRoomId();

            List<InvitedMembers> invitedMembersList = invitedMembersRepository.findAllByRoomId(roomId);
            for (InvitedMembers invitedMembers : invitedMembersList) {
                if (invitedMembers.getMember().equals(member)) {
                    invitedMembers.setReadCheck(true);
                }
            }

            // 이미 그방에 초대되어 있다면 중복으로 저장을 하지 않게 한다.
            if (!invitedMembersRepository.existsByMemberIdAndRoomId(member.getId(), roomId)) {
                InvitedMembers invitedMembers = new InvitedMembers(roomId, member);
                invitedMembersRepository.save(invitedMembers);
            }
            //받아온 메세지 타입이 QUIT 일때
        } else if (ChatMessage.MessageType.QUIT.equals(messageDto.getType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 나가셨습니다. ");
            if (invitedMembersRepository.findAllByRoomId(messageDto.getRoomId()).size() == 1) {
                invitedMembersRepository.deleteByMemberIdAndRoomId(member.getId(), messageDto.getRoomId());
                chatRoomJpaRepository.deleteByRoomId(messageDto.getRoomId());
                chatMessageJpaRepository.deleteByRoomId(messageDto.getRoomId());
            }

            if (invitedMembersRepository.findAllByRoomId(messageDto.getRoomId()).size() == 2) {
                invitedMembersRepository.deleteByMemberIdAndRoomId(member.getId(), messageDto.getRoomId());
            }
        }
        chatMessageRepository.save(messageDto); // 캐시에 저장 했다.
        ChatMessage chatMessage = new ChatMessage(messageDto, createdAt);
        chatMessageJpaRepository.save(chatMessage); // DB 저장
        sendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }

    public List<ChatMessageDto> getMessages(String roomId) {
        return chatMessageRepository.findAllMessage(roomId);
    }

}