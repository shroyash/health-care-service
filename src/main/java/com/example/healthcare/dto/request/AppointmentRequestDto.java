package com.example.healthcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {
    private UUID doctorId;
    private String doctorName;
    private String patientName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String notes;
    private Long requestId;
    private Long appointmentId;
    private String meetingLink;
    private String meetingToken;
}
