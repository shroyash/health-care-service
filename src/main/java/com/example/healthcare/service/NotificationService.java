package com.example.healthcare.service;

import com.example.healthcare.dto.Notification;
import com.example.healthcare.model.NotificationEntity;
import com.example.healthcare.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository repository;

    public void sendNotification(String recipientId, String title, String message, String type) {
        // Save to database
        NotificationEntity entity = new NotificationEntity();
        entity.setRecipientId(recipientId);
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setType(type);
        repository.save(entity);

        // Send via WebSocket if online
        Notification wsNotification = new Notification(recipientId, title, message);
        messagingTemplate.convertAndSend("/topic/notifications/" + recipientId, wsNotification);
    }

    public List<NotificationEntity> getUnreadNotifications(String recipientId) {
        return repository.findByRecipientIdAndIsReadFalse(recipientId);
    }

    public void markAsRead(Long notificationId) {
        repository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }
}
