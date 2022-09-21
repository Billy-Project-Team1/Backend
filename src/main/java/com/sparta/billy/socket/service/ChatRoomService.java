package com.sparta.billy.socket.service;

import com.sparta.billy.dto.ResponseDto;
import com.sparta.billy.model.Member;
import com.sparta.billy.model.Post;
import com.sparta.billy.socket.dto.MemberDto;
import com.sparta.billy.socket.model.ChatRoom;
import com.sparta.billy.socket.model.InvitedMembers;
import com.sparta.billy.socket.repository.ChatRoomJpaRepository;
import com.sparta.billy.socket.repository.ChatRoomRepository;
import com.sparta.billy.socket.repository.InvitedMembersRepository;
import com.sparta.billy.util.Check;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatRoomService {

    private final Check check;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final InvitedMembersRepository invitedMembersRepository;
    /*
     * destination 에서 roomid 가져오기
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }

    @Transactional
    public ResponseDto<?> createChatRoom(Long postId, HttpServletRequest request) {
        Member member = check.validateMember(request);
        check.tokenCheck(request, member);
        Post post = check.getCurrentPost(postId);
        check.checkPost(post);

        Optional<ChatRoom> alreadyChatRoom = chatRoomJpaRepository.findByNicknameAndPost(member.getNickname(), post);
        Optional<InvitedMembers> alreadyMember = invitedMembersRepository.findByMemberIdAndRoomId(member.getId(), alreadyChatRoom.get().getRoomId());
        if (alreadyChatRoom.isPresent() && alreadyMember.isPresent()) {
            return ResponseDto.success(alreadyChatRoom.get().getRoomId());
        }

        ChatRoom chatRoom = ChatRoom.create(post, member);
        chatRoomRepository.createChatRoom(chatRoom);
        return ResponseDto.success(chatRoom.getRoomId());
    }
}