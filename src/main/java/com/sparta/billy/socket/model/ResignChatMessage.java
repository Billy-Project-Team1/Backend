package com.sparta.billy.socket.model;

import com.sparta.billy.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ResignChatMessage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String roomId; // 방번호 (postId)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessage.MessageType type; // 메시지 타입
    @Column(nullable = false)
    private String sender; // nickname
    @Column(nullable = false)
    private String message; // 메시지
    @Column(nullable = false)
    private String profileUrl;
    @Column
    private Long enterUserCnt;
    @Column(nullable = false)
    private Long memberId;


    public ResignChatMessage(ChatMessage chatMessage) {
        this.roomId = chatMessage.getRoomId();
        this.type = chatMessage.getType();
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
        this.profileUrl = chatMessage.getProfileUrl();
        this.enterUserCnt = chatMessage.getEnterUserCnt();
        this.memberId = chatMessage.getMemberId();
    }
}