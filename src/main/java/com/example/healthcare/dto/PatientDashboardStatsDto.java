package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDashboardStatsDto {
    private long totalUpcomingAppointments;
    private List<PatientAppointmentDto> upcomingAppointments;
}
