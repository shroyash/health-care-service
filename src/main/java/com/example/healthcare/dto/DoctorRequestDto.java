package com.example.healthcare.dto;

import com.example.healthcare.enums.DoctorRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequestDto {
    private UUID doctorReqId;
    private String userName;
    private String email;
    private String doctorLicence;
    private DoctorRequestStatus status;
}
