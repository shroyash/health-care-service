package com.example.healthcare.service;


import com.example.healthcare.model.NotificationEntity;

import java.util.List;

public interface NotificationService {

    void sendNotification(String recipientId, String title, String message, String type);

    List<NotificationEntity> getUnreadNotifications(String recipientId);

    void markAsRead(Long notificationId);
}
