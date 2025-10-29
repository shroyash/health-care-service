package com.example.healthcare.dto;

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
    private String status;
    private String checkupType;
}
