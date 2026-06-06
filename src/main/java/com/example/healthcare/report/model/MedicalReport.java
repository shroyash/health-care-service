package com.example.healthcare.report.model;

import com.example.healthcare.appointment.model.Appointment;
import com.example.healthcare.common.enums.ReportStatus;
import com.example.healthcare.common.enums.ReportType;
import com.example.healthcare.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"appointment", "medicines"})
@Table(name = "medical_reports")
public class MedicalReport extends BaseEntity {

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
    private LocalDateTime finalizedAt;

    @OneToMany(mappedBy = "medicalReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportMedicine> medicines;

    // ✅ equals/hashCode based on id only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalReport)) return false;
        MedicalReport that = (MedicalReport) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}