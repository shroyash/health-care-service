package com.example.healthcare.dto.response;

import com.example.healthcare.enums.AppointmentStatus;
import com.example.healthcare.enums.DoctorCheckType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAppointmentDto {
    private Long appointmentId;
    private UUID doctorId;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private DoctorCheckType checkupType;
    private String meetingLink;
    private AppointmentStatus status;
}
