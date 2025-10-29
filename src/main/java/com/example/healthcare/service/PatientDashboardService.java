package com.example.healthcare.service;

import com.example.healthcare.dto.PatientAppointmentDto;
import java.util.List;

public interface PatientDashboardService {

    long getTotalUpcomingAppointments(Long patientId);

    List<PatientAppointmentDto> getUpcomingAppointments(Long patientId);
}
