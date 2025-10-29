package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.PatientAppointmentDto;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.service.PatientDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl implements PatientDashboardService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public long getTotalUpcomingAppointments(Long patientId) {
        return appointmentRepository.countUpcomingAppointmentsByPatient(patientId);
    }

    @Override
    public List<PatientAppointmentDto> getUpcomingAppointments(Long patientId) {
        return appointmentRepository.findUpcomingAppointmentsByPatient(patientId);
    }
}
