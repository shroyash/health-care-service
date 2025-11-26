package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardStatsDto {

    private long totalAppointmentsToday;
    private long totalDoctors;
    private long totalPatients;
    private long pendingDoctorApprovals;
}
