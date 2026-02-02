package com.example.healthcare.service;


import com.example.healthcare.dto.CheckupTypeCountDto;
import com.example.healthcare.dto.DailyAppointmentCount;
import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.dto.DoctorDashboardStatsDto;

import java.util.List;
import java.util.UUID;

public interface DoctorDashboardService {
    DoctorDashboardStatsDto getDoctorDashboard(UUID userId);
    List<DoctorAppointmentDto> getAppointments(UUID userId);
    List<DailyAppointmentCount> getDoctorWeeklyAppointments(Long doctorId);
    List<CheckupTypeCountDto> getAppointmentCountByCheckupType(Long doctorId);
}
