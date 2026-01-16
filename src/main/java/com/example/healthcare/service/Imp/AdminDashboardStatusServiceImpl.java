package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.*;
import com.example.healthcare.enums.Gender;
import com.example.healthcare.enums.Status;
import com.example.healthcare.feign.AuthServiceClient;
import com.example.healthcare.enums.AppointmentStatus;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.AdminDashboardStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
public class AdminDashboardStatusServiceImpl implements AdminDashboardStatusService {

    private final DoctorProfileRepository doctorRepository;
    private final PatientProfileRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthServiceClient authServiceClient;


    public AdminDashboardStatusServiceImpl(
            DoctorProfileRepository doctorRepository,
            PatientProfileRepository patientRepository,
            AppointmentRepository appointmentRepository,
            AuthServiceClient authServiceClient
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.authServiceClient = authServiceClient;
    }

    @Override
    public long getTotalAppointmentsToday() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            return appointmentRepository.countByAppointmentDateBetween(startOfDay, endOfDay);
        } catch (Exception e) {
            log.error("Error fetching total appointments today: {}", e.getMessage(), e);
            return 0; // fallback value
        }
    }

    @Override
    public long getTotalDoctors() {
        try {
            return doctorRepository.count();
        } catch (Exception e) {
            log.error("Error fetching total doctors: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public long getTotalPatients() {
        try {
            return patientRepository.count();
        } catch (Exception e) {
            log.error("Error fetching total patients: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public PatientsStats getPatientStats() {
        long activePatients = patientRepository.countByStatus(Status.ACTIVE);
        long totalPatients = patientRepository.count();
        long totalCompletedAppointments = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);
        return new PatientsStats(activePatients,totalPatients,totalCompletedAppointments);
    }

    @Override
    public long getPendingDoctorApprovals() {
        try {
            return authServiceClient.getPendingDoctorApprovals();
        } catch (Exception e) {
            log.error("Failed to fetch pending doctor approvals: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<AppointmentFullDto> getRecentAppointments() {
        try {
            List<AppointmentFullDto> appointments = appointmentRepository.findAllAppointmentsWithDoctorAndPatient();
            return appointments != null ? appointments : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching recent appointments: {}", e.getMessage(), e);
            return Collections.emptyList(); // fallback
        }
    }

    @Override
    public GenderCountResponseDto getPatientsGenderCount() {
            long male = 0;
            long female = 0;

            List<GenderCountDto> result = patientRepository.countByGender();

            for (GenderCountDto dto : result) {
                if (dto.getGender() == Gender.MALE) {
                    male = dto.getCount();
                } else if (dto.getGender() == Gender.FEMALE) {
                    female = dto.getCount();
                }
            }

            return new GenderCountResponseDto(male, female);
        }

    @Override
    public List<WeeklyAppointmentCountDto> getWeeklyAppointments() {

        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = endOfWeek.atTime(LocalTime.MAX);

        List<WeeklyAppointmentCountDto> dbResult =
                appointmentRepository.countAppointmentsByDayOfWeek(start, end);

        Map<String, Long> map = new HashMap<>();
        for (WeeklyAppointmentCountDto dto : dbResult) {
            map.put(dto.getDay().toUpperCase(), dto.getCount());
        }

        List<WeeklyAppointmentCountDto> response = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            response.add(
                    new WeeklyAppointmentCountDto(
                            day.name(),
                            map.getOrDefault(day.name(), 0L)
                    )
            );
        }

        return response;
    }

}