package com.example.healthcare.dto;

import com.example.healthcare.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientAppointmentDto {
    private Long appointmentId;
    private UUID doctorId;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private String startTime;
    private String endTime;
    private String checkupType;
    private String meetingLink;
    private AppointmentStatus status;       // CONFIRMED, PENDING, etc.
}
