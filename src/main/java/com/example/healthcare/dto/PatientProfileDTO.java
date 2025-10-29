package com.example.healthcare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientProfileDTO {
    private Long patientId;
    private String fullName;
    private String email;
    private String contactNumber;
    private String status;
}
