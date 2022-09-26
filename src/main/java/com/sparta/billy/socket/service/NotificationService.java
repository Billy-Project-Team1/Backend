package com.sparta.billy.socket.service;

import com.sparta.billy.exception.ex.NotFoundChatRoomException;
import com.sparta.billy.model.UserDetailsImpl;
import com.sparta.billy.socket.dto.NotificationDto;
import com.sparta.billy.socket.model.ChatMessage;
import com.sparta.billy.socket.model.ChatRoom;
import com.sparta.billy.socket.model.InvitedMembers;
import com.sparta.billy.socket.repository.ChatMessageJpaRepository;
import com.sparta.billy.socket.repository.ChatRoomJpaRepository;
import com.sparta.billy.socket.repository.InvitedMembersRepository;
import com.sparta.billy.util.NotificationComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Transactional
    public List<NotificationDto> getNotification(UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        Boolean readCheck = false;

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByMemberIdAndReadCheck(memberId, readCheck);

        for (InvitedMembers invitedMember : invitedMembers) {
            List<ChatMessage> findChatMessageDtoList = chatMessageJpaRepository.findAllByRoomId(invitedMember.getRoomId());
            for (ChatMessage findChatMessageDto : findChatMessageDtoList) {
                if (Objects.equals(String.valueOf(invitedMember.getRoomId()), findChatMessageDto.getRoomId())) {
                    if (invitedMember.getReadCheckTime().isBefore(findChatMessageDto.getCreatedAt())) {
                        ChatRoom chatRoom = chatRoomJpaRepository.findByRoomId(invitedMember.getRoomId());
                        if (chatRoom == null) {
                            throw new NotFoundChatRoomException();
                        }
                        NotificationDto notificationDto = new NotificationDto();
                        if (findChatMessageDto.getMessage().isEmpty()) {
                            notificationDto.setMessage("메세지가 없어요😲");
                        } else {
                            notificationDto.setMessage(findChatMessageDto.getMessage());
                        }
                        notificationDto.setNickname(findChatMessageDto.getSender());
                        notificationDto.setRoomId(findChatMessageDto.getRoomId());
                        notificationDtoList.add(notificationDto);
                    }
                }
            }
        }
        notificationDtoList.sort(new NotificationComparator());
        return notificationDtoList;
    }

}
