package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DoctorProfileUpdateDto {
    private String email;
    private String specialization;
    private Integer yearsOfExperience;
    private String contactNumber;
}
