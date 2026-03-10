package com.example.healthcare.service;


import com.example.healthcare.dto.response.CheckupTypeCountDto;
import com.example.healthcare.dto.response.DailyAppointmentCount;
import com.example.healthcare.dto.response.DoctorAppointmentDto;
import com.example.healthcare.dto.response.DoctorDashboardStatsDto;

import java.util.List;
import java.util.UUID;

public interface DoctorDashboardService {
    DoctorDashboardStatsDto getDoctorDashboard(UUID userId);
    List<DoctorAppointmentDto> getAppointments(UUID userId);
    List<DoctorAppointmentDto> getAllAppointments(UUID userId);
    List<DailyAppointmentCount> getDoctorWeeklyAppointments(Long doctorId);
    List<CheckupTypeCountDto> getAppointmentCountByCheckupType(Long doctorId);
}
