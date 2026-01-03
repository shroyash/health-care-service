package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.DoctorWithScheduleDto;
import com.example.healthcare.dto.PatientAppointmentDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.model.PatientProfile;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.PatientDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public List<PatientAppointmentDto> getUpcomingAppointments(UUID userId) {
        // find patient profile by userId
        PatientProfile patientProfile = patientProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        // fetch upcoming appointments for that patient
        return appointmentRepository.findUpcomingAppointmentsByPatient(userId);
    }

    @Override
    public List<DoctorWithScheduleDto> getAllAvailableDoctor() {
        // Fetch all doctors with schedules (use EntityGraph or fetch join)
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();

        // Map to DTO
        return doctors.stream().map(doctor -> DoctorWithScheduleDto.builder()
                .doctorProfileId(doctor.getId())
                .name(doctor.getFullName())
                .specialty(doctor.getSpecialization())
                .email(doctor.getEmail())
                .phone(doctor.getContactNumber())
                .schedules(
                        doctor.getSchedules().stream()
                                .map(schedule -> DoctorWithScheduleDto.ScheduleDto.builder()
                                        .dayOfWeek(schedule.getDayOfWeek())
                                        .startTime(schedule.getStartTime())
                                        .endTime(schedule.getEndTime())
                                        .available(schedule.isAvailable())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build()
        ).collect(Collectors.toList());
    }





}
