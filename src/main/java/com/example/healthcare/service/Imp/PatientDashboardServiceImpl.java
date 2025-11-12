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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl implements PatientDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    public long getTotalUpcomingAppointments(Long userId) {
        // find patient profile by userId
        PatientProfile patientProfile = patientProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        Long patientProfileId = patientProfile.getPatientProfileId();

        // count appointments for that patient
        return appointmentRepository.countUpcomingAppointmentsByPatient(patientProfileId);
    }

    @Override
    public List<PatientAppointmentDto> getUpcomingAppointments(Long userId) {
        // find patient profile by userId
        PatientProfile patientProfile = patientProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        Long patientProfileId = patientProfile.getPatientProfileId();

        // fetch upcoming appointments for that patient
        return appointmentRepository.findUpcomingAppointmentsByPatient(patientProfileId);
    }

    @Override
    public List<DoctorWithScheduleDto> getAllAvailableDoctor() {
        // Fetch all doctors with schedules (use EntityGraph or fetch join)
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();

        // Map to DTO
        return doctors.stream().map(doctor -> DoctorWithScheduleDto.builder()
                .doctorProfileId(doctor.getDoctorProfileId())
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
