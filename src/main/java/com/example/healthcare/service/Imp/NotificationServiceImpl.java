package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.Notification;
import com.example.healthcare.model.NotificationEntity;
import com.example.healthcare.repository.NotificationRepository;
import com.example.healthcare.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository repository;

    @Override
    public void sendNotification(String recipientId, String title, String message, String type) {
        // Save to DB
        NotificationEntity entity = new NotificationEntity();
        entity.setRecipientId(recipientId);
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setType(type);
        repository.save(entity);

        // Send via WebSocket
        Notification wsNotification = new Notification(recipientId, title, message);
        messagingTemplate.convertAndSend("/topic/notifications/" + recipientId, wsNotification);
    }

    @Override
    public List<NotificationEntity> getUnreadNotifications(String recipientId) {
        return repository.findByRecipientIdAndIsReadFalse(recipientId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        repository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            repository.save(notification);
        });
    }
}
