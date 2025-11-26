package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorWithScheduleDto;
import com.example.healthcare.dto.PatientAppointmentDto;
import java.util.List;

public interface PatientDashboardService {

    long getTotalUpcomingAppointments(Long userId);

    List<PatientAppointmentDto> getUpcomingAppointments(Long userId);

    List<DoctorWithScheduleDto> getAllAvailableDoctor();
}
