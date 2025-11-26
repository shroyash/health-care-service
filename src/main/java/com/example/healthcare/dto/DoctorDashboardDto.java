package com.example.healthcare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorDashboardDto {
    Long todayAppointments;
    Long patientPendingReq;
    Long totalPatients;
}
