package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "appointment_requests")
public class AppointmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private String patientFullName; // Who requested

    private Long doctorId;
    private String doctorFullName; // For which doctor

    private String status;      // PENDING, CONFIRMED, REJECTED
    private String notes;       // patient message

    private LocalDateTime createdAt = LocalDateTime.now();
}
