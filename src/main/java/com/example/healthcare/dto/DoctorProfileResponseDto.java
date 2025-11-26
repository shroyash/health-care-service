package com.example.healthcare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorProfileResponseDto {
    private Long doctorProfileId;
    private String fullName;
    private String email;
    private String specialization;
    private int yearsOfExperience;
    private String workingAT;
    private String contactNumber;
}