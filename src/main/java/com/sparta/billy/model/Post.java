package com.sparta.billy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.billy.dto.PostDto.PostUploadRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int deposit;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String detailLocation;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    public void update(PostUploadRequestDto requestDto) {
        if (requestDto.getTitle() != null) {
            this.title = requestDto.getTitle();
        }
        if (requestDto.getContent() != null) {
            this.content = requestDto.getContent();
        }
        if (requestDto.getDeposit() != 0) {
            this.deposit = requestDto.getDeposit();
        }
        if (requestDto.getPrice() != 0) {
            this.price = requestDto.getPrice();
        }
        if (requestDto.getLocation() != null) {
            this.location = requestDto.getLocation();
        }
        if (requestDto.getLatitude() != null) {
            this.latitude = requestDto.getLatitude();
        }
        if (requestDto.getLongitude() != null) {
            this.longitude = requestDto.getLongitude();
        }
        if (requestDto.getLongitude() != null) {
            this.longitude = requestDto.getLongitude();
        }
    }
}
