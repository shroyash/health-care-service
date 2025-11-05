package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAppointmentDto {
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private LocalDateTime appointmentDate;
    private String startTime;
    private String endTime;
    private String checkupType;
    private String meetingLink;
}
