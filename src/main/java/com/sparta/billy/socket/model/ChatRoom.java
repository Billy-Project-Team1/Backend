package com.sparta.billy.socket.model;

import com.sparta.billy.model.Post;
import com.sparta.billy.socket.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    @GeneratedValue
    private String roomId;

    @Column(nullable = false)
    private Long postId;
    @Column(nullable = false)
    private String nickname;

    //채팅방 생성
    public static ChatRoom create(Post post, MemberDto memberDto) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        chatRoom.nickname=memberDto.getNickName();
        chatRoom.postId = post.getId();
        return chatRoom;
    }
}