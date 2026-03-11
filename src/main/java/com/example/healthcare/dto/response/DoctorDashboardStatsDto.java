package com.example.healthcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDashboardStatsDto {
    private long totalAppointmentsToday;
    private long pendingRequests;
    private long totalPatients;
    private long reportsTaken;
}
