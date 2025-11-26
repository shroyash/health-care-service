package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.AppointmentRequestDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.exceptions.UnauthorizedException;
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


    // CREATE REQUEST
    @Override
    @Transactional
    public void createRequest(AppointmentRequestDto dto, String token) {

        String patientUserName = JwtUtils.extractUserNameFromToken(token);
        Long userId = JwtUtils.extractUserIdFromToken(token);

        PatientProfile patient = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        AppointmentRequest request = AppointmentRequest.builder()
                .patientId(patient.getPatientProfileId())
                .patientFullName(patientUserName)
                .doctorId(dto.getDoctorId())
                .doctorFullName(dto.getDoctorName())
                .day(dto.getDay())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(AppointmentRequestStatus.PENDING)
                .notes(dto.getNotes())
                .requestedAt(LocalDateTime.now())   // FIXED
                .build();

        requestRepository.save(request);

        // Notify doctor
        notificationService.sendNotification(
                dto.getDoctorId().toString(),
                "New Appointment Request",
                "Patient " + patientUserName + " requested an appointment.",
                "APPOINTMENT_REQUEST"
        );
    }



    // GET REQUESTS FOR DOCTOR
    @Override
    public List<AppointmentRequestDto> getRequestsForDoctor(Long doctorUserId) {

        DoctorProfile doctor = doctorProfileRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        return requestRepository.findByDoctorId(doctor.getDoctorProfileId())
                .stream()
                .map(this::toDto)
                .toList();
    }



    // GET REQUESTS FOR PATIENT
    @Override
    public List<AppointmentRequestDto> getRequestsForPatient(String token) {

        Long userId = JwtUtils.extractUserIdFromToken(token);

        PatientProfile patient = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        return requestRepository.findByPatientId(patient.getPatientProfileId())
                .stream()
                .map(this::toDto)
                .toList();
    }



    // UPDATE STATUS
    @Override
    @Transactional
    public AppointmentRequestDto updateStatus(Long requestId, String status, String token) {

        Long doctorUserId = JwtUtils.extractUserIdFromToken(token);

        DoctorProfile doctor = doctorProfileRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        AppointmentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getDoctorId().equals(doctor.getDoctorProfileId())) {
            throw new UnauthorizedException("Unauthorized to update this request");
        }

        AppointmentRequestStatus newStatus = AppointmentRequestStatus.valueOf(status.toUpperCase());
        request.setStatus(newStatus);

        requestRepository.save(request); // SAVE STATUS



        // If approved → create actual appointment
        if (newStatus == AppointmentRequestStatus.APPROVED) {

            PatientProfile patient = patientProfileRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

            DoctorSchedule schedule = doctor.getSchedules().stream()
                    .filter(s -> s.getDayOfWeek().equalsIgnoreCase(request.getDay())
                            && s.getStartTime().equals(request.getStartTime())
                            && s.isAvailable())
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Selected schedule not available"));

            schedule.setAvailable(false);
            schedule.setUpdatedAt(LocalDateTime.now());

            doctorProfileRepository.save(doctor); // IMPORTANT — FIXES schedule update



            LocalDateTime appointmentDate = LocalDateTime.of(
                    LocalDateTime.now().toLocalDate(),
                    LocalTime.parse(request.getStartTime())
            );

            Appointment appointment = Appointment.builder()
                    .doctor(doctor)
                    .patient(patient)
                    .schedule(schedule)
                    .appointmentDate(appointmentDate)
                    .status(AppointmentStatus.SCHEDULED)
                    .checkupType("General")
                    .createdAt(LocalDateTime.now())
                    .build();

            appointmentRepository.save(appointment);
        }



        // Notify patient
        notificationService.sendNotification(
                doctor.getUserId().toString(),  // FIXED USER ID
                "Appointment " + newStatus.name(),
                "Your appointment with Dr. " + request.getDoctorFullName()
                        + " is " + newStatus.name().toLowerCase(),
                "APPOINTMENT_STATUS"
        );

        return toDto(request);
    }



    // DTO MAPPER
    private AppointmentRequestDto toDto(AppointmentRequest r) {
        return AppointmentRequestDto.builder()
                .requestId(r.getId())
                .doctorId(r.getDoctorId())
                .doctorName(r.getDoctorFullName())
                .patientName(r.getPatientFullName())
                .day(r.getDay())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .notes(r.getNotes())
                .status(r.getStatus().name())
                .build();
    }
}
