package com.example.healthcare.report.dto;

import com.example.healthcare.common.enums.ReportStatus;
import com.example.healthcare.common.enums.ReportType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ReportResponseDto {
    private Long id;
    private Long appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private String patientName;
    private String doctorName;
    private String title;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private String notes;
    private ReportType reportType;
    private ReportStatus status;
    private String reportUrl;
    private List<ReportMedicineDto> medicines;
    private LocalDateTime finalizedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}