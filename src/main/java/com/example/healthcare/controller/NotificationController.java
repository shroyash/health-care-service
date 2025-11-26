package com.example.healthcare.controller;

import com.example.healthcare.dto.Notification;
import com.example.healthcare.model.NotificationEntity;
import com.example.healthcare.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public void sendNotification(@RequestBody Notification notification) {
        service.sendNotification(
                notification.getDoctorId(),
                notification.getTitle(),
                notification.getMessage(),
                "APPOINTMENT_REQUEST"
        );
    }

    // Fetch unread notifications
    @GetMapping("/unread/{recipientId}")
    public List<NotificationEntity> getUnread(@PathVariable String recipientId) {
        return service.getUnreadNotifications(recipientId);
    }

    // Mark notification as read
    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
    }
}

