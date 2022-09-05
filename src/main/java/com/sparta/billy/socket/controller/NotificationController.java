package com.sparta.billy.socket.controller;

import com.sparta.billy.model.UserDetailsImpl;
import com.sparta.billy.socket.dto.NotificationDto;
import com.sparta.billy.socket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/notification")
    public List<NotificationDto> getNotification(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return notificationService.getNotification(userDetails);
    }
}