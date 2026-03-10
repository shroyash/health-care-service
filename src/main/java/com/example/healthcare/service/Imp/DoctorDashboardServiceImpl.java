package com.example.healthcare.service.Imp;


import com.example.healthcare.dto.response.CheckupTypeCountDto;
import com.example.healthcare.dto.response.DailyAppointmentCount;
import com.example.healthcare.dto.response.DoctorAppointmentDto;
import com.example.healthcare.dto.response.DoctorDashboardStatsDto;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.service.DoctorDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorDashboardServiceImpl implements DoctorDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentRequestRepository requestRepository;

    public DoctorDashboardServiceImpl(AppointmentRepository appointmentRepository,
                                      AppointmentRequestRepository requestRepository, DoctorProfileRepository doctorProfileRepository) {
        this.appointmentRepository = appointmentRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public DoctorDashboardStatsDto getDoctorDashboard(UUID doctorProfileId) {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        long totalAppointmentsToday = appointmentRepository
                .countByDoctorIdAndAppointmentDateBetween(doctorProfileId, startOfDay, endOfDay);

        long totalPatients = appointmentRepository
                .countDistinctPatientsByDoctor(doctorProfileId);

        long totalAppointmentsReq = requestRepository
                .countPendingAppointmentRequestsByDoctor(doctorProfileId);

        return DoctorDashboardStatsDto.builder()
                .totalAppointmentsToday(totalAppointmentsToday)
                .totalPatients(totalPatients)
                .pendingRequests(totalAppointmentsReq)
                .build();
    }

    @Transactional
    @Override
    public List<DoctorAppointmentDto> getAppointments(UUID userID) {
        appointmentRepository.cancelExpiredForDoctor(userID, LocalDateTime.now());
        return appointmentRepository
                .findUpcomingAppointmentsByDoctor(userID);
    }

    @Transactional
    @Override
    public List<DoctorAppointmentDto> getAllAppointments(UUID userId) {
        appointmentRepository.cancelExpiredForDoctor(userId, LocalDateTime.now());
        return appointmentRepository
                .findAllAppointmentsByDoctor(userId);
    }

    @Override
    public List<DailyAppointmentCount> getDoctorWeeklyAppointments(Long doctorId) {

        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

        return appointmentRepository.getWeeklyAppointmentCount(
                doctorId,
                startDateTime,
                endDateTime
        );
    }

    @Override
    public List<CheckupTypeCountDto> getAppointmentCountByCheckupType(Long doctorId) {
        return appointmentRepository.countAppointmentsByCheckupType(doctorId);
    }

}
