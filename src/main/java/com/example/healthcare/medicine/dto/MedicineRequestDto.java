package com.example.healthcare.medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequestDto {
    private String name;
    private String description;
    private String dosage;
    private String category;
    private String sideEffects;
    private String manufacturer;
}