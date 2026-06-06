package com.example.healthcare.appointment.dto.response;


import com.example.healthcare.appointment.enums.AppointmentStatus;
import com.example.healthcare.common.enums.DoctorCheckType;
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
    private DoctorCheckType checkupType;
    private String meetingLink;
}

