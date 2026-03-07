package com.example.healthcare.service;

import com.example.healthcare.dto.response.DoctorWithScheduleDto;
import com.example.healthcare.dto.response.PatientAppointmentDto;
import java.util.List;
import java.util.UUID;

public interface PatientDashboardService {

    long getTotalUpcomingAppointments(UUID userId);

    List<PatientAppointmentDto> getAppointments(UUID userId);

    List<PatientAppointmentDto> getUpcomingAppointments(UUID userId);

    List<DoctorWithScheduleDto> getAllAvailableDoctor();
}
