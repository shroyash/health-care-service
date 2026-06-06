
package com.example.healthcare.dashboard.service.imp;

import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.appointmentRequest.repository.AppointmentRequestRepository;
import com.example.healthcare.dashboard.dto.DoctorDashboardStatsDto;
import com.example.healthcare.dashboard.service.DoctorDashboardService;
import com.example.healthcare.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorDashboardServiceImpl implements DoctorDashboardService {

    // Facade — coordinates 3 domains
    private final AppointmentRepository appointmentRepository;
    private final AppointmentRequestRepository requestRepository;
    private final ReportRepository reportRepository;

    @Override
    public DoctorDashboardStatsDto getDashboardStats(UUID doctorId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        long totalAppointmentsToday = appointmentRepository
                .countByDoctorIdAndAppointmentDateBetween(
                        doctorId, startOfDay, endOfDay);

        long totalPatients = appointmentRepository
                .countDistinctPatientsByDoctor(doctorId);

        long pendingRequests = requestRepository
                .countPendingAppointmentRequestsByDoctor(doctorId);

        long reportsWritten = reportRepository
                .countByDoctorId(doctorId);

        return DoctorDashboardStatsDto.builder()
                .totalAppointmentsToday(totalAppointmentsToday)
                .totalPatients(totalPatients)
                .pendingRequests(pendingRequests)
                .reportsTaken(reportsWritten)
                .build();
    }
}