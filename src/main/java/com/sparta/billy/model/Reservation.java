package com.sparta.billy.model;

import com.sparta.billy.dto.ReservationDto.ReservationStateRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "jully_id")
    @ManyToOne
    private Member jully; // 물건 대여해주는 회원

    @JoinColumn(name = "billy_id", nullable = false)
    @ManyToOne
    private Member billy; // 물건 대여하는 회원

    @Column(nullable = false)
    private int state;

    @Column
    private boolean delivery;

    @Column
    private String cancelMessage;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column
    private boolean reviewCheck;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public void changeState(ReservationStateRequestDto requestDto) {
        this.cancelMessage = requestDto.getCancelMessage();
        this.state = requestDto.getState();
    }

    public void setDelivery() {
        this.delivery = true;
    }
}
