package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDashboardStatsDto {
    private long totalAppointmentsToday;
    private long pendingRequests;
    private long totalPatients;
    private long totalPatientsThisWeek;
    private long reportsThisMonth;
}
