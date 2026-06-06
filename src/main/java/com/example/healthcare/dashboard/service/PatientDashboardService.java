
package com.example.healthcare.dashboard.service;

import com.example.healthcare.dashboard.dto.PatientDashboardStatsDto;
import java.util.UUID;

public interface PatientDashboardService {
    PatientDashboardStatsDto getDashboardStats(UUID patientId);
}