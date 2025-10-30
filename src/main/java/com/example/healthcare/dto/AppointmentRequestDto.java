package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private String day;
    private String startTime;
    private String endTime;
    private String status;
    private String notes;
    Long requestId;
}
