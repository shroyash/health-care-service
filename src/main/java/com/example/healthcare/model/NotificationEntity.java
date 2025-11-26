package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientId; // doctorId or adminId
    private String title;
    private String message;
    private String type; //  e.g., "APPOINTMENT_REQUEST"
    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

}
