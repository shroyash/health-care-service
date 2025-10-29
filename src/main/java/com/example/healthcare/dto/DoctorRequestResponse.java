package com.example.healthcare.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequestResponse {
    private String message;
    private String status;
    private Long doctorReqId;
    private String doctorName;
}
