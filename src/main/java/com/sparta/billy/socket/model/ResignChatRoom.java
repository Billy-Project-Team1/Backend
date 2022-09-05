package com.sparta.billy.socket.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ResignChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String roomId;
    @Column(nullable = false)
    private String nickname;

    public ResignChatRoom(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.nickname = chatRoom.getNickname();
    }
}