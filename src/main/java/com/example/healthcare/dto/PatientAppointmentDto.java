package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAppointmentDto {
    private Long appointmentId;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private String status;

}
