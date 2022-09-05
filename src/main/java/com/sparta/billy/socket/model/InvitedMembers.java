package com.sparta.billy.socket.model;

import com.sparta.billy.model.Member;
import com.sparta.billy.model.Timestamped;
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
    private Long postId;
    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @Column
    private Boolean readCheck;
    @Column
    private LocalDateTime readCheckTime;


    public InvitedMembers(Long postId, Member member) {
        this.postId = postId;
        this.member = member;
        this.readCheck =true;
    }

}
