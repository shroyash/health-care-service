// appointment/dto/response/MeetingAccessDto.java
package com.example.healthcare.appointment.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingAccessDto {
    private Long appointmentId;
    private String userId;
    private String doctorName;
    private String patientName;
    private String appointmentTime;
    private boolean canJoin;
}