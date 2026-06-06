// appointmentrequest/dto/response/AppointmentRequestResponseDto.java
package com.example.healthcare.appointmentRequest.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestResponseDto {

    private Long requestId;
    private UUID doctorId;
    private String doctorName;
    private String patientName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;
    private String status;

    // populated only when APPROVED
    private Long appointmentId;
    private String meetingLink;
    private String meetingToken;
}