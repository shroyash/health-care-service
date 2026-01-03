package com.example.healthcare.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PatientProfileDTO {
    private UUID patientId;
    private String fullName;
    private String email;
    private String contactNumber;
    private String status;
    private String profileImgUrl;
}
