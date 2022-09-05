package com.sparta.billy.socket.service;

import com.sparta.billy.exception.PostApiException;
import com.sparta.billy.model.Post;
import com.sparta.billy.model.UserDetailsImpl;
import com.sparta.billy.repository.PostRepository;
import com.sparta.billy.socket.dto.NotificationDto;
import com.sparta.billy.socket.model.ChatMessage;
import com.sparta.billy.socket.model.InvitedMembers;
import com.sparta.billy.socket.repository.ChatMessageJpaRepository;
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

    private final PostRepository postRepository;

    @Transactional
    public List<NotificationDto> getNotification(UserDetailsImpl userDetails) {
        Long userId = userDetails.getMember().getId();
        Boolean readCheck = false;

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<InvitedMembers> invitedMembers = invitedMembersRepository.findAllByMemberIdAndReadCheck(userId, readCheck);

        for (InvitedMembers invitedMember : invitedMembers) {
            List<ChatMessage> findChatMessageDtoList = chatMessageJpaRepository.findAllByRoomId(String.valueOf(invitedMember.getPostId()));
            for (ChatMessage findChatMessageDto : findChatMessageDtoList) {
                if (Objects.equals(String.valueOf(invitedMember.getPostId()), findChatMessageDto.getRoomId())) {
                    if (invitedMember.getReadCheckTime().isBefore(findChatMessageDto.getCreatedAt())) {
                        Post post = postRepository.findById(Long.valueOf(findChatMessageDto.getRoomId())).orElseThrow(
                                () -> new PostApiException("존재하지 않는 게시물 입니다.")
                        );
                        NotificationDto notificationDto = new NotificationDto();
                        if (findChatMessageDto.getMessage().isEmpty()) {
                            notificationDto.setMessage("파일이 왔어요😲");
                        } else {
                            notificationDto.setMessage(findChatMessageDto.getMessage());
                        }
                        notificationDto.setNickname(findChatMessageDto.getSender());
                        notificationDto.setRoomId(findChatMessageDto.getRoomId());
                        notificationDto.setTitle(post.getTitle());
                        notificationDtoList.add(notificationDto);
                    }
                }
            }
        }
        notificationDtoList.sort(new NotificationComparator());
        return notificationDtoList;
    }
}
