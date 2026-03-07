package com.example.healthcare.dto.request;

import lombok.Data;

@Data
public class ReportMedicineDto {
    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
}