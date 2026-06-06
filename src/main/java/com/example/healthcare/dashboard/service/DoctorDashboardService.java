
package com.example.healthcare.dashboard.service;

import com.example.healthcare.dashboard.dto.DoctorDashboardStatsDto;
import java.util.UUID;

public interface DoctorDashboardService {
    DoctorDashboardStatsDto getDashboardStats(UUID doctorId);
}