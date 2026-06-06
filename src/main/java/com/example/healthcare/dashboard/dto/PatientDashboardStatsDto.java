package com.example.healthcare.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDashboardStatsDto {
    private long totalUpcomingAppointments;
    private long totalActiveDoctor;
    private long totalReportWritten;
}