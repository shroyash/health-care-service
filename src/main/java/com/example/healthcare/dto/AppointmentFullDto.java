package com.example.healthcare.dto;

import com.example.healthcare.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFullDto {

    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String checkupType;
    private String meetingLink;
}
