package com.example.healthcare.model;

import com.example.healthcare.enums.ReportStatus;
import com.example.healthcare.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "medical_reports")
public class MedicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private UUID patientId;
    private UUID doctorId;

    private String title;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private String notes;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String reportUrl;

    @OneToMany(mappedBy = "medicalReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportMedicine> medicines;

    private LocalDateTime finalizedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
