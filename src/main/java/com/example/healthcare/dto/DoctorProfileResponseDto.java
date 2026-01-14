package com.example.healthcare.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class DoctorProfileResponseDto {

    private UUID doctorProfileId;
    private String fullName;
    private String email;
    private String specialization;
    private int yearsOfExperience;
    private String workingAT;
    private String contactNumber;
    private String dateOfBirth;
    private String gender;
    private String country;
    private String profileImgUrl;
    private String status;
}