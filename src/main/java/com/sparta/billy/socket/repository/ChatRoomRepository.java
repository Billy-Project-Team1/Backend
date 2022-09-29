package com.sparta.billy.socket.repository;

import com.sparta.billy.exception.ex.NotFoundChatRoomException;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.PostImgUrl;
import com.sparta.billy.repository.PostImgUrlRepository;
import com.sparta.billy.socket.dto.ChatListMessageDto;
import com.sparta.billy.socket.dto.ChatRoomResponseDto;
import com.sparta.billy.socket.model.ChatMessage;
import com.sparta.billy.socket.model.ChatRoom;
import com.sparta.billy.socket.model.InvitedMembers;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final PostImgUrlRepository postImgUrlRepository;
    private final Check check;

    //내가 참여한 모든 채팅방 목록 조회
    @Transactional
    public ChatListMessageDto findAllRoom(HttpServletRequest request) {
        Member member = check.validateMember(request);
        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByMemberId(member.getId());
        if (invitedMembers.isEmpty()) { throw new NotFoundChatRoomException(); }

        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        for (InvitedMembers invitedMember : invitedMembers) {
            if (invitedMember.getReadCheck()) {
                invitedMember.setReadCheck(false);
                invitedMember.setReadCheckTime(LocalDateTime.now());
            }

            //채팅방 있는지 확인
            ChatRoom chatRoom = chatRoomJpaRepository.findByRoomId(invitedMember.getRoomId());

            ChatMessage lastMessage = chatMessageJpaRepository.findTop1ByRoomIdAndTypeOrderByCreatedAtDesc(invitedMember.getRoomId(), ChatMessage.MessageType.TALK);
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
            if (lastMessage.getMessage().isEmpty()) {
                chatRoomResponseDto.setLastMessage(lastMessage.getMessage());
            } else{
                chatRoomResponseDto.setLastMessage(lastMessage.getMessage());
            }
            LocalDateTime createdAt = lastMessage.getCreatedAt();
            String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
            List<InvitedMembers> twoInvitedMembers = invitedMembersRepository.findByRoomId(chatRoom.getRoomId());

            for (InvitedMembers otherMember : twoInvitedMembers) {
                if (!otherMember.getMember().equals(member)) {
                    String otherMemberNickname = otherMember.getMember().getNickname();
                    String otherMemberProfileUrl = otherMember.getMember().getProfileUrl();
                    chatRoomResponseDto.setOtherNickname(otherMemberNickname);
                    chatRoomResponseDto.setProfileUrl(otherMemberProfileUrl);
                }
            }
            List<PostImgUrl> postImgUrlList = postImgUrlRepository.findAllByPost(chatRoom.getPost());

            chatRoomResponseDto.setLastMessageTime(createdAtString);
            chatRoomResponseDto.setUserId(member.getUserId());
            chatRoomResponseDto.setPostId(chatRoom.getPost().getId());
            chatRoomResponseDto.setPostImgUrl(postImgUrlList.get(0).getImgUrl());
            chatRoomResponseDto.setRoomId(chatRoom.getRoomId());
            chatRoomResponseDtoList.add(chatRoomResponseDto);
        }
        return new ChatListMessageDto(chatRoomResponseDtoList);
    }

    public void createChatRoom(ChatRoom chatRoom) {
        chatRoomJpaRepository.save(chatRoom); // DB 저장
    }
}