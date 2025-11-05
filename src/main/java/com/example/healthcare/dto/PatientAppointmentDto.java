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
public class PatientAppointmentDto {
    private Long appointmentId;
    private Long doctorId;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private String startTime;
    private String endTime;
    private String checkupType;
    private String meetingLink;
    private String status;       // CONFIRMED, PENDING, etc.
}
