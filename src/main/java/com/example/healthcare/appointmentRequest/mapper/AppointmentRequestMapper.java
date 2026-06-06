// appointmentrequest/mapper/AppointmentRequestMapper.java
package com.example.healthcare.appointmentRequest.mapper;


import com.example.healthcare.appointmentRequest.dto.response.AppointmentRequestResponseDto;
import com.example.healthcare.appointmentRequest.model.AppointmentRequest;
import org.springframework.stereotype.Component;

@Component
public class AppointmentRequestMapper {

    public AppointmentRequestResponseDto toDto(AppointmentRequest r) {
        return AppointmentRequestResponseDto.builder()
                .requestId(r.getId())
                .doctorId(r.getDoctorId())
                .doctorName(r.getDoctorFullName())
                .patientName(r.getPatientFullName())
                .date(r.getDate())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .notes(r.getNotes())
                .status(r.getStatus().name())
                .build();
    }
}