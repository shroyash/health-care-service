package com.example.healthcare.service;


import com.example.healthcare.dto.DoctorDashboardStatsDto;

public interface DoctorDashboardService {
    DoctorDashboardStatsDto getDoctorDashboard(Long doctorId);
}
