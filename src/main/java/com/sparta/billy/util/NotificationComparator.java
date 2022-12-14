package com.sparta.billy.util;

import com.sparta.billy.socket.dto.NotificationDto;

import java.util.Comparator;

public class NotificationComparator implements Comparator<NotificationDto> {
    @Override
    public int compare(NotificationDto n1, NotificationDto n2) {
        if (n1.getCreatedAt().isBefore(n2.getCreatedAt())) {
            return 1;
        } else if (n1.getCreatedAt().isAfter(n2.getCreatedAt())) {
            return -1;
        }
        return 0;
    }
}