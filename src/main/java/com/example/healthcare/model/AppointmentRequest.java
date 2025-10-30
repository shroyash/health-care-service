package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "appointment_requests")
public class AppointmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private String patientFullName; // Who requested

    private Long doctorId;
    private String doctorFullName;

    private String day;

    private String startTime;

    private String endTime;

    private String status;      // PENDING, CONFIRMED, REJECTED
    private String notes;       // patient message

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
