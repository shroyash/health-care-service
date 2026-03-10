package com.example.healthcare.service;

import com.example.healthcare.dto.response.DoctorAppointmentDto;
import com.example.healthcare.dto.response.PatientAppointmentDto;
import com.example.healthcare.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentHistoryService {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public List<PatientAppointmentDto> getPatientHistory(UUID patientId) {
        appointmentRepository.cancelExpiredForPatient(patientId, LocalDateTime.now());
        return appointmentRepository.findPatientHistory(patientId);
    }

    @Transactional
    public List<DoctorAppointmentDto> getDoctorHistory(UUID doctorId) {
        appointmentRepository.cancelExpiredForDoctor(doctorId, LocalDateTime.now());
        return appointmentRepository.findDoctorHistory(doctorId);
    }
}