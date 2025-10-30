package com.example.healthcare.service.Imp;


import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.dto.DoctorDashboardStatsDto;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
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
    private final DoctorProfileRepository doctorProfileRepository;

    public DoctorDashboardServiceImpl(AppointmentRepository appointmentRepository,
                                      AppointmentRequestRepository requestRepository, DoctorProfileRepository doctorProfileRepository) {
        this.appointmentRepository = appointmentRepository;
        this.requestRepository = requestRepository;
        this.doctorProfileRepository = doctorProfileRepository;
    }

    @Override
    public DoctorDashboardStatsDto getDoctorDashboard(Long userId) {

        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user"));

        Long doctorProfileId = doctorProfile.getDoctorProfileId();

        // 1. Calculate today's range
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 2. Fetch stats
        long totalAppointmentsToday = appointmentRepository
                .countByDoctorIdAndAppointmentDateBetween(doctorProfileId, startOfDay, endOfDay);

        long totalPatients = appointmentRepository
                .countDistinctPatientsByDoctor(doctorProfileId);

        long pendingRequests = requestRepository
                .countByDoctorIdAndStatus(doctorProfileId, "PENDING");

        // 4. Build DTO
        return DoctorDashboardStatsDto.builder()
                .totalAppointmentsToday(totalAppointmentsToday)
                .totalPatients(totalPatients)
                .pendingRequests(pendingRequests)
                .build();
    }

    @Override
    public List<DoctorAppointmentDto> getAppointments(Long userID){
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userID)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user"));

        Long doctorProfileId = doctorProfile.getDoctorProfileId();
        return appointmentRepository
                .findConfirmedAppointmentsWithScheduleByDoctor(doctorProfileId);
    }
}
