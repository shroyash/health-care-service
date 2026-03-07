package com.example.healthcare.dto.response;

import com.example.healthcare.enums.AppointmentStatus;
import com.example.healthcare.enums.DoctorCheckType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFullDto {
    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private DoctorCheckType checkupType;
    private String meetingLink;
}

