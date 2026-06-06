// appointment/mapper/AppointmentMapper.java
package com.example.healthcare.appointment.mapper;

import com.example.healthcare.appointment.dto.response.AppointmentResponseDto;
import com.example.healthcare.appointment.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponseDto toDto(Appointment a) {
        return AppointmentResponseDto.builder()
                .appointmentId(a.getId())
                .doctorName(a.getDoctor().getFullName())
                .patientName(a.getPatient().getFullName())
                .appointmentDate(a.getAppointmentDate())
                .meetingLink(a.getMeetingLink())
                .status(a.getStatus().name())
                .build();
    }
}