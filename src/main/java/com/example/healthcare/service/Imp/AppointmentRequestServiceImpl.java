package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.request.AppointmentRequestDto;
import com.example.healthcare.enums.AppointmentRequestStatus;
import com.example.healthcare.enums.AppointmentStatus;
import com.example.healthcare.enums.DoctorCheckType;
import com.example.healthcare.exceptions.DuplicateRequestException;
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

        // ✅ Check duplicate before inserting
        boolean alreadyExists = requestRepository.existsByPatientIdAndDoctorIdAndDateAndStartTime(
                patient.getId(),
                dto.getDoctorId(),
                dto.getDate(),
                dto.getStartTime()
        );

        if (alreadyExists) {
            throw new DuplicateRequestException(
                    "You already requested this slot. Please wait for the doctor to respond."
            );
        }

        AppointmentRequest request = AppointmentRequest.builder()
                .patientId(patient.getId())
                .patientFullName(patientUserName)
                .doctorId(dto.getDoctorId())
                .doctorFullName(dto.getDoctorName())
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(AppointmentRequestStatus.PENDING)
                .notes(dto.getNotes())
                .requestedAt(LocalDateTime.now())
                .build();

        requestRepository.save(request);

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

    @Override
    @Transactional
    public AppointmentRequestDto updateStatus(Long requestId, String status, String token) {

        UUID doctorUserId = JwtUtils.extractUserIdFromToken(token);

        DoctorProfile doctor = doctorProfileRepository.findById(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        AppointmentRequest request = requestRepository.findByIdForUpdate(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getDoctorId().equals(doctor.getId())) {
            throw new UnauthorizedException("Unauthorized to update this request");
        }

        AppointmentRequestStatus newStatus =
                AppointmentRequestStatus.valueOf(status.toUpperCase());

        // If already in desired state, return existing data
        if (request.getStatus() == newStatus) {
            AppointmentRequestDto dto = toDto(request);

            if (newStatus == AppointmentRequestStatus.APPROVED) {
                appointmentRepository.findByAppointmentRequest(request)
                        .ifPresent(existing -> {
                            dto.setAppointmentId(existing.getId());
                            dto.setMeetingLink(existing.getMeetingLink());
                            dto.setMeetingToken(existing.getMeetingToken());
                        });
            }
            return dto;
        }

        // Block changing terminal states
        if (request.getStatus() == AppointmentRequestStatus.APPROVED ||
                request.getStatus() == AppointmentRequestStatus.REJECTED) {
            throw new IllegalStateException(
                    "Request is already " + request.getStatus().name().toLowerCase()
                            + " and cannot be changed"
            );
        }

        request.setStatus(newStatus);
        requestRepository.save(request);

        AppointmentRequestDto dto = toDto(request);

        if (newStatus == AppointmentRequestStatus.APPROVED) {

            PatientProfile patient = patientProfileRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

            // ✅ Check if a schedule slot already exists for this date and time
            DoctorSchedule schedule = doctor.getSchedules().stream()
                    .filter(s -> s.getScheduleDate().equals(request.getDate())
                            && s.getStartTime().equals(request.getStartTime()))
                    .findFirst()
                    .orElse(null);

            if (schedule != null) {
                // Slot exists — make sure it's not already booked
                if (!schedule.isAvailable()) {
                    throw new IllegalStateException(
                            "Doctor already has a booked appointment at this time"
                    );
                }
                // Mark existing slot as booked
                schedule.setAvailable(false);
                schedule.setUpdatedAt(LocalDateTime.now());

            } else {
                // No slot found — custom request, create and record in schedule
                schedule = DoctorSchedule.builder()
                        .doctorProfile(doctor)
                        .scheduleDate(request.getDate())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .available(false)
                        .isLocked(false)
                        .build();

                doctor.getSchedules().add(schedule);
            }

            doctorProfileRepository.save(doctor);

            LocalDateTime appointmentDate = LocalDateTime.of(
                    request.getDate(),
                    request.getStartTime()
            );

            String meetingToken = UUID.randomUUID().toString();

            Appointment appointment = Appointment.builder()
                    .doctor(doctor)
                    .patient(patient)
                    .schedule(schedule)
                    .appointmentRequest(request)
                    .appointmentDate(appointmentDate)
                    .meetingToken(meetingToken)
                    .status(AppointmentStatus.SCHEDULED)
                    .checkupType(DoctorCheckType.GENERAL)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            appointmentRepository.save(appointment);

            String meetingLink = "https://localhost:3000/meet/" + appointment.getId()
                    + "?token=" + meetingToken;

            appointment.setMeetingLink(meetingLink);
            appointment.setUpdatedAt(LocalDateTime.now());
            appointmentRepository.save(appointment);

            dto.setAppointmentId(appointment.getId());
            dto.setMeetingLink(meetingLink);
            dto.setMeetingToken(meetingToken);

            String subject = "Appointment Scheduled";
            String body = "Your appointment with Dr. " + doctor.getFullName()
                    + " is scheduled for " + appointmentDate
                    + ".\n\nJoin the meeting using this link:\n" + meetingLink;

            emailService.sendEmail(patient.getEmail(), subject, body);
            emailService.sendEmail(doctor.getEmail(), subject, body);

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
