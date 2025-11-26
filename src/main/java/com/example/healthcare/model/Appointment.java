package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientProfile patient;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private DoctorSchedule schedule;

    private LocalDateTime appointmentDate;
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String checkupType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
