
package com.example.healthcare.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDashboardStatsDto {
    private long totalAppointmentsToday;
    private long totalPatients;
    private long pendingRequests;
    private long reportsTaken;
}