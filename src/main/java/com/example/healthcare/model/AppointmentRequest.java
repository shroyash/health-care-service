package com.example.healthcare.model;

import com.example.healthcare.enums.AppointmentRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

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

    private UUID patientId;
    private String patientFullName;

    private UUID doctorId;
    private String doctorFullName;

    private LocalDate date;       // changed
    private LocalTime startTime;  // changed
    private LocalTime endTime;    // changed

    @Enumerated(EnumType.STRING)
    private AppointmentRequestStatus status;

    private String notes;

    @Builder.Default
    private LocalDateTime requestedAt = LocalDateTime.now();
}
