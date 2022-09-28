package com.sparta.billy.socket.service;

import com.sparta.billy.socket.dto.NotificationDto;
import com.sparta.billy.socket.model.ChatMessage;
import com.sparta.billy.socket.model.InvitedMembers;
import com.sparta.billy.socket.repository.ChatMessageJpaRepository;
import com.sparta.billy.socket.repository.ChatRoomJpaRepository;
import com.sparta.billy.socket.repository.InvitedMembersRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final InvitedMembersRepository invitedMembersRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final Check check;

    @Transactional
    public List<NotificationDto> getNotification(HttpServletRequest request) {
        Long memberId = check.validateMember(request).getId();
        Boolean readCheck = false;

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByMemberIdAndReadCheck(memberId, readCheck);

        for (InvitedMembers invitedMember : invitedMembers) {
            List<ChatMessage> findChatMessageDtoList = chatMessageJpaRepository.findAllByRoomId(invitedMember.getRoomId());
            for (ChatMessage findChatMessageDto : findChatMessageDtoList) {
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
        return notificationDtoList;
    }

}
