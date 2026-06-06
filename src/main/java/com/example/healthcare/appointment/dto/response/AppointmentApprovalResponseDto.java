
package com.example.healthcare.appointment.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentApprovalResponseDto {

    private Long requestId;
    private Long appointmentId;
    private UUID doctorId;
    private String doctorName;
    private String patientName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String meetingLink;
    private String meetingToken;
}