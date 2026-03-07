package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.request.AppointmentRequestDto;
import com.example.healthcare.enums.AppointmentRequestStatus;
import com.example.healthcare.enums.AppointmentStatus;
import com.example.healthcare.enums.DoctorCheckType;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.exceptions.UnauthorizedException;
import com.example.healthcare.model.*;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.AppointmentRequestService;
import com.example.healthcare.service.EmailService;
import com.example.healthcare.service.NotificationService;
import com.example.healthcare.utils.JwtUtils;

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
public class AppointmentRequestServiceImpl implements AppointmentRequestService {

    private final AppointmentRequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    // CREATE REQUEST
    @Override
    @Transactional
    public void createRequest(AppointmentRequestDto dto, String token) {

        String patientUserName = JwtUtils.extractUserNameFromToken(token);
        UUID userId = JwtUtils.extractUserIdFromToken(token);

        PatientProfile patient = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        // Use actual date instead of day
        AppointmentRequest request = AppointmentRequest.builder()
                .patientId(patient.getId())
                .patientFullName(patientUserName)
                .doctorId(dto.getDoctorId())
                .doctorFullName(dto.getDoctorName())
                .date(dto.getDate())  // <-- updated
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(AppointmentRequestStatus.PENDING)
                .notes(dto.getNotes())
                .requestedAt(LocalDateTime.now())
                .build();

        requestRepository.save(request);

        // Notify doctor
        notificationService.sendNotification(
                dto.getDoctorId().toString(),
                "New Appointment Request",
                "Patient " + patientUserName + " requested an appointment on " + dto.getDate(),
                "APPOINTMENT_REQUEST"
        );
    }

    // GET REQUESTS FOR DOCTOR
    @Override
    public List<AppointmentRequestDto> getRequestsForDoctor(UUID userId) {

        DoctorProfile doctor = doctorProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        return requestRepository.findByDoctorId(doctor.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    // GET REQUESTS FOR PATIENT
    @Override
    public List<AppointmentRequestDto> getRequestsForPatient(String token) {

        UUID userId = JwtUtils.extractUserIdFromToken(token);

        PatientProfile patient = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        return requestRepository.findByPatientId(patient.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

        // UPDATE STATUS (APPROVE / REJECT)
        @Override
        @Transactional
        public AppointmentRequestDto updateStatus(Long requestId, String status, String token) {

            UUID doctorUserId = JwtUtils.extractUserIdFromToken(token);

            DoctorProfile doctor = doctorProfileRepository.findById(doctorUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

            AppointmentRequest request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

            // Check ownership
            if (!request.getDoctorId().equals(doctor.getId())) {
                throw new UnauthorizedException("Unauthorized to update this request");
            }

            AppointmentRequestStatus newStatus =
                    AppointmentRequestStatus.valueOf(status.toUpperCase());

            request.setStatus(newStatus);
            requestRepository.save(request);

            AppointmentRequestDto dto = toDto(request);

            if (newStatus == AppointmentRequestStatus.APPROVED) {

                PatientProfile patient = patientProfileRepository.findById(request.getPatientId())
                        .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

                // Find available schedule by exact date + startTime
                DoctorSchedule schedule = doctor.getSchedules().stream()
                        .filter(s -> s.getScheduleDate().equals(request.getDate())
                                && s.getStartTime().equals(request.getStartTime())
                                && s.isAvailable())
                        .findFirst()
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Selected schedule not available"));

                // Mark schedule as booked
                schedule.setAvailable(false);
                schedule.setUpdatedAt(LocalDateTime.now());
                doctorProfileRepository.save(doctor);

                LocalDateTime appointmentDate = LocalDateTime.of(
                        request.getDate(),       // LocalDate
                        request.getStartTime()   // LocalTime
                );

                String meetingToken = UUID.randomUUID().toString();

                Appointment appointment = Appointment.builder()
                        .doctor(doctor)
                        .patient(patient)
                        .schedule(schedule)
                        .appointmentDate(appointmentDate)
                        .meetingToken(meetingToken)
                        .status(AppointmentStatus.SCHEDULED)
                        .checkupType(DoctorCheckType.GENERAL)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                appointmentRepository.save(appointment);

                // Build meeting link
                String meetingLink = "https://localhost:3000/meet/" + appointment.getId()
                        + "?token=" + meetingToken;

                appointment.setMeetingLink(meetingLink);
                appointment.setUpdatedAt(LocalDateTime.now());
                appointmentRepository.save(appointment);

                dto.setAppointmentId(appointment.getId());
                dto.setMeetingLink(meetingLink);
                dto.setMeetingToken(meetingToken);

                // Send emails
                String subject = "Appointment Scheduled";
                String body = "Your appointment with Dr. " + doctor.getFullName()
                        + " is scheduled for " + appointmentDate
                        + ".\n\nJoin the meeting using this link:\n" + meetingLink;

                emailService.sendEmail(patient.getEmail(), subject, body);
                emailService.sendEmail(doctor.getEmail(), subject, body);

                // Send notifications
                notificationService.sendNotification(
                        patient.getId().toString(),
                        "Appointment Confirmed",
                        "Join meeting: " + meetingLink,
                        "APPOINTMENT_MEETING"
                );

                notificationService.sendNotification(
                        doctor.getId().toString(),
                        "Appointment Confirmed",
                        "Join meeting: " + meetingLink,
                        "APPOINTMENT_MEETING"
                );
            }

            // Notify patient about status change
            notificationService.sendNotification(
                    request.getPatientId().toString(),
                    "Appointment " + newStatus.name(),
                    "Your appointment status is now: " + newStatus.name().toLowerCase(),
                    "APPOINTMENT_STATUS"
            );

            return dto;
        }

    // DTO Mapper
    private AppointmentRequestDto toDto(AppointmentRequest r) {
        return AppointmentRequestDto.builder()
                .requestId(r.getId())
                .doctorId(r.getDoctorId())
                .doctorName(r.getDoctorFullName())
                .patientName(r.getPatientFullName())
                .date(r.getDate())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .notes(r.getNotes())
                .status(r.getStatus().name())
                .build();
    }
}
