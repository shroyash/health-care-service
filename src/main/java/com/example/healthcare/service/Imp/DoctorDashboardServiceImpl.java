package com.example.healthcare.service.Imp;


import com.example.healthcare.dto.CheckupTypeCountDto;
import com.example.healthcare.dto.DailyAppointmentCount;
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
import java.util.UUID;

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
    public DoctorDashboardStatsDto getDoctorDashboard(UUID doctorProfileId) {

        DoctorProfile doctorProfile = doctorProfileRepository.findById(doctorProfileId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user"));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // 2. Fetch stats
        long totalAppointmentsToday = appointmentRepository
                .countByDoctorIdAndAppointmentDateBetween(doctorProfileId, startOfDay, endOfDay);

        long totalPatients = appointmentRepository
                .countDistinctPatientsByDoctor(doctorProfileId);

//        long pendingRequests = requestRepository
//                .countByDoctorIdAndStatus(doctorProfileId, AppointmentRequestStatus.PENDING);

        // 4. Build DTO
        return DoctorDashboardStatsDto.builder()
                .totalAppointmentsToday(totalAppointmentsToday)
                .totalPatients(totalPatients)
                .pendingRequests(0)
                .build();
    }

    @Override
    public List<DoctorAppointmentDto> getAppointments(UUID userID) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user"));

        return appointmentRepository
                .findUpcomingAppointmentsByDoctor(userID);
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
