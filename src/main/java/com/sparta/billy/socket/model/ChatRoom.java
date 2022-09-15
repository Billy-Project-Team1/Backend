package com.sparta.billy.socket.model;

import com.sparta.billy.model.Post;
import com.sparta.billy.socket.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

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
    private String roomId;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @Column(nullable = false)
    private String nickname;

    //채팅방 생성
    public static ChatRoom create(Post post, MemberDto memberDto) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString().substring(0,8);
        chatRoom.nickname=memberDto.getNickName();
        chatRoom.post = post;
        return chatRoom;
    }
}