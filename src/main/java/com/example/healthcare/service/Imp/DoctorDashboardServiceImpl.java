package com.example.healthcare.service.Imp;


import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.dto.DoctorDashboardStatsDto;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.service.DoctorDashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DoctorDashboardServiceImpl implements DoctorDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentRequestRepository requestRepository;

    public DoctorDashboardServiceImpl(AppointmentRepository appointmentRepository,
                                      AppointmentRequestRepository requestRepository) {
        this.appointmentRepository = appointmentRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public DoctorDashboardStatsDto getDoctorDashboard(Long doctorId) {
        // 1. Calculate today's range
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 2. Fetch stats
        long totalAppointmentsToday = appointmentRepository
                .countByDoctorIdAndAppointmentDateBetween(doctorId, startOfDay, endOfDay);

        long totalPatients = appointmentRepository
                .countDistinctPatientsByDoctor(doctorId);

        long pendingRequests = requestRepository
                .countByDoctorIdAndStatus(doctorId, "PENDING");

        // 3. Fetch confirmed appointments with schedule
        List<DoctorAppointmentDto> appointments = appointmentRepository
                .findConfirmedAppointmentsWithScheduleByDoctor(doctorId);

        // 4. Build DTO
        return DoctorDashboardStatsDto.builder()
                .totalAppointmentsToday(totalAppointmentsToday)
                .totalPatients(totalPatients)
                .pendingRequests(pendingRequests)
                .appointments(appointments)
                .build();
    }
}
