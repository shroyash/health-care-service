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
@Table(
        name = "appointment_requests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_patient_doctor_date_time",
                        columnNames = {"patient_id", "doctor_id", "date", "start_time"}
                )
        }
)
public class AppointmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID patientId;
    private String patientFullName;

    private UUID doctorId;
    private String doctorFullName;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentRequestStatus status;

    private String notes;

    @Builder.Default
    private LocalDateTime requestedAt = LocalDateTime.now();
}
