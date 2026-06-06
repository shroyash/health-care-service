// appointmentrequest/dto/request/AppointmentRequestDto.java
package com.example.healthcare.appointmentRequest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {

    @NotNull
    private UUID doctorId;

    @NotNull
    private String doctorName;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private String notes;
}