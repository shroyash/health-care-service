
package com.example.healthcare.dashboard.service.imp;

import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.dashboard.dto.AdminDashboardStatsDto;
import com.example.healthcare.dashboard.feign.AuthServiceClient;
import com.example.healthcare.dashboard.service.AdminDashboardService;
import com.example.healthcare.doctor.repository.DoctorProfileRepository;
import com.example.healthcare.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    // Facade — coordinates 4 domains
    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final AuthServiceClient authServiceClient;

    @Override
    public AdminDashboardStatsDto getDashboardStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        long totalAppointmentsToday = appointmentRepository
                .countByAppointmentDateBetween(startOfDay, endOfDay);
        long totalDoctors = doctorProfileRepository.count();
        long totalPatients = patientProfileRepository.count();
        long pendingApprovals = fetchPendingApprovals();

        return new AdminDashboardStatsDto(
                totalAppointmentsToday,
                totalDoctors,
                totalPatients,
                pendingApprovals);
    }

    private long fetchPendingApprovals() {
        try {
            return authServiceClient.getPendingDoctorApprovals();
        } catch (Exception e) {
            log.error("Failed to fetch pending approvals: {}", e.getMessage());
            return 0L;
        }
    }
}