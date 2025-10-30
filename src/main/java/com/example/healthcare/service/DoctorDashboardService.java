package com.example.healthcare.service;


import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.dto.DoctorDashboardStatsDto;

import java.util.List;

public interface DoctorDashboardService {
    DoctorDashboardStatsDto getDoctorDashboard(Long userId);
    List<DoctorAppointmentDto> getAppointments(Long userId);
}
