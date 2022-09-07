package com.sparta.billy.socket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.billy.exception.ex.MemberNotFoundException;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.UserDetailsImpl;
import com.sparta.billy.repository.MemberRepository;
import com.sparta.billy.repository.PostRepository;
import com.sparta.billy.socket.dto.ChatMessageDto;
import com.sparta.billy.socket.dto.MemberDetailDto;
import com.sparta.billy.socket.dto.MemberinfoDto;
import com.sparta.billy.socket.model.*;
import com.sparta.billy.socket.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository userRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ResignChatRoomJpaRepository resignChatRoomJpaRepository;
    private final PostRepository postRepository;
    private final ResignChatMessageJpaRepository resignChatMessageJpaRepository;


    @Transactional
    public void save(ChatMessageDto messageDto, Long pk) throws JsonProcessingException {
        // 토큰에서 유저 아이디 가져오기
        Member member = userRepository.findById(pk).orElseThrow(
                () -> new NullPointerException("존재하지 않는 사용자 입니다!")
        );
        LocalDateTime createdAt = LocalDateTime.now();
        String formatDate = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));
        Long enterUserCnt = chatMessageRepository.getUserCnt(messageDto.getRoomId());
        messageDto.setEnterUserCnt(enterUserCnt);
        messageDto.setSender(member.getNickname());
        messageDto.setProfileUrl(member.getProfileUrl());
        messageDto.setCreatedAt(formatDate);
        messageDto.setMemberId(member.getId());
        messageDto.setQuitOwner(false);

        //받아온 메세지의 타입이 ENTER 일때
        if (ChatMessage.MessageType.ENTER.equals(messageDto.getType())) {
            chatRoomRepository.enterChatRoom(messageDto.getRoomId());
            messageDto.setMessage( messageDto.getSender() + "님이 입장하셨습니다.");
            String roomId = messageDto.getRoomId();


            List<InvitedMembers> invitedUsersList = invitedMembersRepository.findAllByPostId(Long.parseLong(roomId));
            for (InvitedMembers invitedMembers : invitedUsersList) {
                if (invitedMembers.getMember().equals(member)) {
                    invitedMembers.setReadCheck(true);
                }
            }
            // 이미 그방에 초대되어 있다면 중복으로 저장을 하지 않게 한다.
            if (!invitedMembersRepository.existsByMemberIdAndPostId(member.getId(), Long.parseLong(roomId))) {
                InvitedMembers invitedUsers = new InvitedMembers(Long.parseLong(roomId), member);
                invitedMembersRepository.save(invitedUsers);
            }
            //받아온 메세지 타입이 QUIT 일때
        } else if (ChatMessage.MessageType.QUIT.equals(messageDto.getType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 나가셨습니다.");
            if (invitedMembersRepository.existsByMemberIdAndPostId(member.getId(), Long.parseLong(messageDto.getRoomId()))) {
                invitedMembersRepository.deleteByMemberIdAndPostId(member.getId(), Long.parseLong(messageDto.getRoomId()));
            }
            if (!postRepository.existsById(Long.parseLong(messageDto.getRoomId()))) {
                ResignChatRoom chatRoom = resignChatRoomJpaRepository.findByRoomId(messageDto.getRoomId());
                if (chatRoom.getNickname().equals(member.getNickname())) {
                    messageDto.setQuitOwner(true);
                    messageDto.setMessage("(방장) " + messageDto.getSender() + "님이 나가셨습니다. " +
                            "더 이상 대화를 할 수 없으며 채팅방을 나가면 다시 입장할 수 없습니다.");
                    postRepository.deleteById(Long.parseLong(messageDto.getRoomId()));
                    member.setIsOwner(false);
                    ChatRoom findChatRoom = chatRoomJpaRepository.findByRoomId(messageDto.getRoomId());
                    List<ChatMessage> chatMessage = chatMessageJpaRepository.findAllByRoomId(messageDto.getRoomId());
                    ResignChatRoom resignChatRoom = new ResignChatRoom(findChatRoom);
                    resignChatRoomJpaRepository.save(resignChatRoom);
                    for (ChatMessage message : chatMessage) {
                        ResignChatMessage resignChatMessage = new ResignChatMessage(message);
                        resignChatMessageJpaRepository.save(resignChatMessage);
                    }
                    chatMessageJpaRepository.deleteByRoomId(messageDto.getRoomId());
                    chatRoomJpaRepository.deleteByRoomId(messageDto.getRoomId());
                }
            }else {
                ChatRoom chatRoom = chatRoomJpaRepository.findByRoomId(messageDto.getRoomId());
                if (chatRoom.getNickname().equals(member.getNickname())) {
                    messageDto.setQuitOwner(true);
                    messageDto.setMessage("(방장) " + messageDto.getSender() + "님이 나가셨습니다. " +
                            "더 이상 대화를 할 수 없으며 채팅방을 나가면 다시 입장할 수 없습니다.");
                    postRepository.deleteById(Long.parseLong(messageDto.getRoomId()));
                    member.setIsOwner(false);
                    ChatRoom findChatRoom = chatRoomJpaRepository.findByRoomId(messageDto.getRoomId());
                    List<ChatMessage> chatMessage = chatMessageJpaRepository.findAllByRoomId(messageDto.getRoomId());
                    ResignChatRoom resignChatRoom = new ResignChatRoom(findChatRoom);
                    resignChatRoomJpaRepository.save(resignChatRoom);
                    for (ChatMessage message : chatMessage) {
                        ResignChatMessage resignChatMessage = new ResignChatMessage(message);
                        resignChatMessageJpaRepository.save(resignChatMessage);
                    }
                    chatMessageJpaRepository.deleteByRoomId(messageDto.getRoomId());
                    chatRoomJpaRepository.deleteByRoomId(messageDto.getRoomId());
                }
            }
            chatMessageJpaRepository.deleteByRoomId(messageDto.getRoomId());
        }
        chatMessageRepository.save(messageDto); // 캐시에 저장 했다.
        ChatMessage chatMessage = new ChatMessage(messageDto, createdAt);
        chatMessageJpaRepository.save(chatMessage); // DB 저장
        // Websocket 에 발행된 메시지를 redis 로 발행한다(publish)
        redisPublisher.publish(ChatRoomRepository.getTopic(messageDto.getRoomId()), messageDto);
    }

    //redis에 저장되어있는 message 들 출력
    public List<ChatMessageDto> getMessages(String roomId) {
        return chatMessageRepository.findAllMessage(roomId);
    }

    //채팅방에 참여한 사용자 정보 조회
    public List<MemberinfoDto> getUserinfo(UserDetailsImpl userDetails, String roomId) {
        userRepository.findById(userDetails.getMember().getId()).orElseThrow(
                MemberNotFoundException::new
        );
        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByPostId(Long.parseLong(roomId));
        List<MemberinfoDto> members = new ArrayList<>();
        for (InvitedMembers invitedMember : invitedMembers) {
            Member member = invitedMember.getMember();
            members.add(new MemberinfoDto(member.getNickname(), member.getProfileUrl(), member.getId()));
        }
        return members;
    }

    //유저 정보 상세조회 (채팅방 안에서)
    public ResponseEntity<MemberDetailDto> getUserDetails(String roomId, Long memberId) {
        Member member = userRepository.findById(memberId).orElseThrow(
                MemberNotFoundException::new
        );
        ChatRoom chatRoom = chatRoomJpaRepository.findByRoomId(roomId);

        if (chatRoom.getNickname().equals(member.getNickname())) {
            return new ResponseEntity<>(new MemberDetailDto(true, "유저 정보 조회 성공", member, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MemberDetailDto(true, "유저 정보 조회 성공", member, false), HttpStatus.OK);
        }

    }
}