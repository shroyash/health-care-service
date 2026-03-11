package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.response.*;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.model.PatientProfile;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.PatientDashboardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl implements PatientDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    public long getTotalUpcomingAppointments(UUID userId) {
        // find patient profile by userId
        PatientProfile patientProfile = patientProfileRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        // count appointments for that patient
        return appointmentRepository.countUpcomingAppointmentsByPatient(userId);
    }

    @Transactional
    @Override
    public List<PatientAppointmentDto> getUpcomingAppointments(UUID userId) {
        appointmentRepository.cancelExpiredForPatient(userId, LocalDateTime.now());
        return appointmentRepository.findUpcomingAppointmentsByPatient(userId, LocalDateTime.now() );
    }

    @Override
    public List<PatientAppointmentDto> getAppointments(UUID userId) {
        return appointmentRepository.findAllAppointmentsByPatient(userId);
    }

    @Override
    public List<DoctorWithScheduleDto> getAllAvailableDoctor() {
        // Fetch all doctors with schedules
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();

        LocalDate today = LocalDate.now();

        // Map to DTO
        return doctors.stream().map(doctor -> DoctorWithScheduleDto.builder()
                .doctorProfileId(doctor.getId())
                .name(doctor.getFullName())
                .specialty(doctor.getSpecialization())
                .email(doctor.getEmail())
                .phone(doctor.getContactNumber())
                .schedules(
                        doctor.getSchedules().stream()
                                // Only future schedules
                                .filter(s -> !s.getScheduleDate().isBefore(today)) // scheduleDate >= today
                                .map(schedule -> DoctorWithScheduleDto.ScheduleDto.builder()
                                        .date(schedule.getScheduleDate().toString()) // send actual date
                                        .startTime(schedule.getStartTime().toString())
                                        .endTime(schedule.getEndTime().toString())
                                        .available(schedule.isAvailable())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build()
        ).collect(Collectors.toList());
    }

    public List<DailyAppointmentCountDto> getPatientWeeklyAppointments(UUID patientId) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

        List<DailyAppointmentCount> raw = appointmentRepository.getPatientWeeklyAppointmentCount(
                patientId,
                startDateTime,
                endDateTime
        );

        return raw.stream()
                .map(item -> new DailyAppointmentCountDto(
                        item.getDate().toString(),
                        item.getCount()
                ))
                .collect(Collectors.toList());
    }

    public List<AppointmentStatusCountDto> getStatusCount(UUID patientId) {
        return appointmentRepository.getStatusCountByPatient(patientId);
    }
}
