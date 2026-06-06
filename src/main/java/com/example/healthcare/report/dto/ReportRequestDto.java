package com.example.healthcare.report.dto;

import com.example.healthcare.common.enums.ReportType;
import lombok.Data;
import java.util.List;

@Data
public class ReportRequestDto {
    private Long appointmentId;
    private String title;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private String notes;
    private ReportType reportType;
    private List<ReportMedicineDto> medicines;
}