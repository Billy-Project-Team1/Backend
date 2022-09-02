package com.sparta.billy.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Date;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private int price;
    private int deposit;
    private Date blockDate;
    private Point location;
    private Double latitude;
    private Double longitude;
}
