package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.AppointmentFullDto;
import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.PatientProfileDTO;
import com.example.healthcare.feign.AuthServiceClient;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.AdminDashboardStatusService;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.service.PatientProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AdminDashboardStatusServiceImpl implements AdminDashboardStatusService {

    private final DoctorProfileRepository doctorRepository;
    private final PatientProfileRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthServiceClient authServiceClient;
    private final DoctorProfileService doctorProfileService;
    private final PatientProfileService patientProfileService;

    public AdminDashboardStatusServiceImpl(
            DoctorProfileRepository doctorRepository,
            PatientProfileRepository patientRepository,
            AppointmentRepository appointmentRepository,
            AuthServiceClient authServiceClient,
            DoctorProfileService doctorProfileService, PatientProfileService patientProfileService
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.authServiceClient = authServiceClient;
        this.doctorProfileService = doctorProfileService;
        this.patientProfileService = patientProfileService;
    }

    @Override
    public long getTotalAppointmentsToday() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            return appointmentRepository.countByAppointmentTimeBetween(startOfDay, endOfDay);
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

}
