package com.sparta.billy.socket.model;

import com.sparta.billy.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InvitedMembers{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String roomId;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private Boolean readCheck;

    @Column
    private LocalDateTime readCheckTime;

    public InvitedMembers(String roomId, Member member) {
        this.roomId = roomId;
        this.member = member;
        this.readCheck = true;
    }

}
