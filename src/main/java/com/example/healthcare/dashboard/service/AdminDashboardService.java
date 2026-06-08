
package com.example.healthcare.dashboard.service;

import com.example.healthcare.appointment.dto.response.WeeklyAppointmentCountDto;
import com.example.healthcare.dashboard.dto.AdminDashboardStatsDto;

import java.util.List;

public interface AdminDashboardService {
    AdminDashboardStatsDto getDashboardStats();
    List<WeeklyAppointmentCountDto> getWeeklyAppointmentCounts();
}