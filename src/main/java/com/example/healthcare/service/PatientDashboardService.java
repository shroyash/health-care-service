package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorWithScheduleDto;
import com.example.healthcare.dto.PatientAppointmentDto;
import java.util.List;
import java.util.UUID;

public interface PatientDashboardService {

    long getTotalUpcomingAppointments(UUID userId);

    List<PatientAppointmentDto> getUpcomingAppointments(UUID userId);

    List<DoctorWithScheduleDto> getAllAvailableDoctor();
}
