package com.example.healthcare.report.dto;

import lombok.Data;

@Data
public class ReportMedicineDto {
    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
}