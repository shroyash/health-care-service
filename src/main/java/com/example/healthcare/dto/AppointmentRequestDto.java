package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {
    private UUID doctorId;
    private String doctorName;
    private String patientName;
    private String date;
    private String startTime;
    private String endTime;
    private String status;
    private String notes;
    private Long requestId;
    private Long appointmentId;
    private String meetingLink;
    private String meetingToken;
}
