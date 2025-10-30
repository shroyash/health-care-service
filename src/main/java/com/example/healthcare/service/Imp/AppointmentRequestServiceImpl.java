package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.AppointmentRequestDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.*;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.AppointmentRequestService;
import com.example.healthcare.service.NotificationService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentRequestServiceImpl implements AppointmentRequestService {

    private final AppointmentRequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createRequest(AppointmentRequestDto dto, String token) {
        Long userId = JwtUtils.extractUserIdFromToken(token);
        PatientProfile patient = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        AppointmentRequest request = AppointmentRequest.builder()
                .doctorId(dto.getDoctorId())
                .doctorFullName(dto.getDoctorName())
                .day(dto.getDay())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .patientFullName(patient.getFullName())
                .status("PENDING")
                .notes(dto.getNotes())
                .createdAt(LocalDateTime.now())
                .build();

        requestRepository.save(request);

        notificationService.sendNotification(
                dto.getDoctorId().toString(),
                "New Appointment Request",
                "Patient " + patient.getFullName() + " requested an appointment.",
                "APPOINTMENT_REQUEST"
        );
    }

    @Override
    public List<AppointmentRequestDto> getRequestsForDoctor(Long doctorUserId) {
        DoctorProfile doctor = doctorProfileRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        Long doctorProfileId = doctor.getDoctorProfileId();

        return requestRepository.findByDoctorId(doctorProfileId)
                .stream()
                .map(request -> AppointmentRequestDto.builder()
                        .requestId(request.getId())
                        .doctorId(request.getDoctorId())
                        .doctorName(request.getDoctorFullName())
                        .patientName(request.getPatientFullName())
                        .day(request.getDay())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .notes(request.getNotes())
                        .status(request.getStatus())
                        .build())
                .toList();
    }

    @Override
    public List<AppointmentRequestDto> getRequestsForPatient(String token) {
        Long userId = JwtUtils.extractUserIdFromToken(token);
        PatientProfile patient = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        return requestRepository.findByPatientId(patient.getPatientProfileId())
                .stream()
                .map(request -> AppointmentRequestDto.builder()
                        .requestId(request.getId())
                        .doctorId(request.getDoctorId())
                        .doctorName(request.getDoctorFullName())
                        .patientName(request.getPatientFullName())
                        .day(request.getDay())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .notes(request.getNotes())
                        .status(request.getStatus())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public AppointmentRequestDto updateStatus(Long requestId, String status, String token) {
        Long doctorUserId = JwtUtils.extractUserIdFromToken(token);
        DoctorProfile doctor = doctorProfileRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        AppointmentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getDoctorId().equals(doctor.getDoctorProfileId())) {
            throw new RuntimeException("Unauthorized to update this request");
        }

        request.setStatus(status);
        requestRepository.save(request);

        // If doctor confirms, create Appointment and mark schedule as booked
        if ("CONFIRMED".equalsIgnoreCase(status)) {
            PatientProfile patient = patientProfileRepository.findByUserId(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

            // Fetch doctor's schedule for the selected day/time
            DoctorSchedule schedule = doctor.getSchedules().stream()
                    .filter(s -> s.getDayOfWeek().equalsIgnoreCase(request.getDay())
                            && s.getStartTime().equals(request.getStartTime())
                            && s.isAvailable())
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Selected schedule not available"));

            // Mark schedule as booked
            schedule.setAvailable(false);
            schedule.setUpdatedAt(LocalDateTime.now());

            // Create appointment
            LocalDateTime appointmentDateTime = LocalDateTime.of(
                    LocalDateTime.now().toLocalDate(), // optional: parse actual date if needed
                    LocalTime.parse(request.getStartTime())
            );

            Appointment appointment = Appointment.builder()
                    .doctor(doctor)
                    .patient(patient)
                    .schedule(schedule)
                    .appointmentDate(appointmentDateTime)
                    .status(AppointmentStatus.CONFIRMED)
                    .checkupType("General")
                    .createdAt(LocalDateTime.now())
                    .build();

            appointmentRepository.save(appointment);
        }

        notificationService.sendNotification(
                request.getPatientFullName(),
                "Appointment " + status,
                "Your appointment with Dr. " + request.getDoctorFullName() + " is " + status.toLowerCase(),
                "APPOINTMENT_STATUS"
        );

        return AppointmentRequestDto.builder()
                .requestId(request.getId())
                .doctorId(request.getDoctorId())
                .doctorName(request.getDoctorFullName())
                .patientName(request.getPatientFullName())
                .day(request.getDay())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .notes(request.getNotes())
                .status(request.getStatus())
                .build();
    }
}
