package com.example.healthcare.service.Imp;


import com.example.healthcare.feign.AuthServiceClient;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.AdminDashboardStatusService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AdminDashboardStatusServiceImpl implements AdminDashboardStatusService {

    private final DoctorProfileRepository doctorRepository;
    private final PatientProfileRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthServiceClient authServiceClient;

    public AdminDashboardStatusServiceImpl(DoctorProfileRepository doctorRepository,
                                           PatientProfileRepository patientRepository,
                                           AppointmentRepository appointmentRepository,
                                           AuthServiceClient authServiceClient) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.authServiceClient = authServiceClient;
    }

    @Override
    public long getTotalAppointmentsToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return appointmentRepository.countByAppointmentTimeBetween(startOfDay, endOfDay);
    }

    @Override
    public long getTotalDoctors() {
        return doctorRepository.count();
    }

    @Override
    public long getTotalPatients() {
        return patientRepository.count();
    }

    @Override
    public long getPendingDoctorApprovals() {
        return authServiceClient.getPendingDoctorApprovals();
    }
}
