package com.example.healthcare.appointment.service;

import com.example.healthcare.appointment.dto.response.MeetingAccessDto;
import com.example.healthcare.appointment.model.Appointment;
import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.common.exceptions.ResourceNotFoundException;
import com.example.healthcare.common.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentAccessService {

    private final AppointmentRepository appointmentRepository;

    public MeetingAccessDto validateAccess(Long appointmentId,
                                           String token,
                                           UUID userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getMeetingToken().equals(token)) {
            throw new UnauthorizedException("Invalid meeting token");
        }

        return MeetingAccessDto.builder()
                .appointmentId(appointment.getId())
                .userId(userId.toString())
                .doctorName(appointment.getDoctor().getFullName())
                .patientName(appointment.getPatient().getFullName())
                .appointmentTime(appointment.getAppointmentDate().toString())
                .canJoin(true)
                .build();
    }
}