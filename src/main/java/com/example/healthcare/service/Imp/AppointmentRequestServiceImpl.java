package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.AppointmentDoctorReceiveResponseDto;
import com.example.healthcare.dto.AppointmentPatientRequestResponseDto;
import com.example.healthcare.dto.AppointmentRequestDto;
import com.example.healthcare.exceptions.BadRequestException;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.AppointmentRequest;
import com.example.healthcare.model.AppointmentStatus;
import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.service.AppointmentRequestService;
import com.example.healthcare.service.NotificationService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentRequestServiceImpl implements AppointmentRequestService {

    private final AppointmentRequestRepository repository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createRequest(AppointmentRequestDto dto, String token) {
        long patientId = JwtUtils.extractUserIdFromToken(token);
        String patientName = JwtUtils.extractUserNameFromToken(token);

        repository.findByPatientIdAndDoctorIdAndStatus(patientId, dto.getDoctorId(), AppointmentStatus.PENDING.toString())
                .ifPresent(existing -> {
                    throw new BadRequestException("You already have a pending appointment request with this doctor");
                });

        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(patientId);
        request.setPatientFullName(patientName);
        request.setDoctorId(dto.getDoctorId());
        request.setDoctorFullName(dto.getDoctorName());
        request.setStatus("PENDING");
        request.setNotes(dto.getNotes());
        request.setCreatedAt(LocalDateTime.now());
        repository.save(request);

        try {
            notificationService.sendNotification(
                    dto.getDoctorId().toString(),
                    "New Appointment Request",
                    "Patient " + patientName + " requested an appointment",
                    "APPOINTMENT_REQUEST"
            );
        } catch (Exception e) {
            log.warn("Failed to send notification to doctor: {}", e.getMessage());
        }
    }

    @Override
    public List<AppointmentPatientRequestResponseDto> getRequestsForDoctor(Long doctorId) {
        return repository.findByDoctorId(doctorId)
                .stream()
                .map(req -> AppointmentPatientRequestResponseDto.builder()
                        .patientId(req.getPatientId())
                        .patientFullName(req.getPatientFullName())
                        .build())
                .toList();
    }

    @Override
    public List<AppointmentDoctorReceiveResponseDto> getRequestsForPatient(String token) {
        long patientId = JwtUtils.extractUserIdFromToken(token);

        return repository.findByPatientId(patientId)
                .stream()
                .map(req -> AppointmentDoctorReceiveResponseDto.builder()
                        .doctorId(req.getDoctorId())
                        .doctorFullName(req.getDoctorFullName())
                        .status(req.getStatus())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public AppointmentDoctorReceiveResponseDto updateStatus(Long requestId, String status, String token) {
        long doctorId = JwtUtils.extractUserIdFromToken(token);

        AppointmentRequest request = repository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getDoctorId().equals(doctorId)) {
            throw new RuntimeException("Unauthorized to update this request");
        }

        request.setStatus(status);
        repository.save(request);

        // Notify patient
        notificationService.sendNotification(
                request.getPatientId().toString(),
                "Appointment " + status,
                "Your appointment with Dr. " + request.getDoctorFullName() + " is " + status.toLowerCase(),
                "APPOINTMENT_STATUS"
        );

        return AppointmentDoctorReceiveResponseDto.builder()
                .doctorId(request.getDoctorId())
                .doctorFullName(request.getDoctorFullName())
                .status(request.getStatus())
                .build();
    }
}
