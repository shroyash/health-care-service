package com.example.healthcare.appointment.service;

import com.example.healthcare.appointment.enums.AppointmentStatus;
import com.example.healthcare.appointment.model.Appointment;
import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.appointmentRequest.model.AppointmentRequest;
import com.example.healthcare.common.enums.DoctorCheckType;
import com.example.healthcare.common.service.EmailService;
import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.model.DoctorSchedule;
import com.example.healthcare.doctor.repository.ScheduleRepository;
import com.example.healthcare.patient.model.PatientProfile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentApprovalService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final EmailService emailService;

    @Value("${app.meeting.base-url}")
    private String meetingBaseUrl;

    @Transactional
    public void approve(AppointmentRequest request,
                        DoctorProfile doctor,
                        PatientProfile patient) {

        validateNotPast(request);

        DoctorSchedule schedule = resolveSchedule(request, doctor);

        Appointment appointment = createAppointment(
                request,
                doctor,
                patient,
                schedule
        );

        String meetingLink = buildMeetingLink(appointment);

        appointment.setMeetingLink(meetingLink);

        appointmentRepository.save(appointment);

        notifyBothParties(
                doctor,
                patient,
                appointment,
                meetingLink
        );
    }

    private void validateNotPast(AppointmentRequest request) {

        LocalDateTime appointmentDateTime =
                LocalDateTime.of(
                        request.getDate(),
                        request.getStartTime()
                );

        if (appointmentDateTime.isBefore(LocalDateTime.now())) {

            throw new IllegalStateException(
                    "Cannot approve appointment in the past"
            );
        }
    }

    private DoctorSchedule resolveSchedule(
            AppointmentRequest request,
            DoctorProfile doctor
    ) {

        validateTime(request);

        List<DoctorSchedule> conflicts =
                scheduleRepository.findConflictingSchedules(
                        doctor.getId(),
                        request.getDate(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        if (!conflicts.isEmpty()) {

            throw new IllegalStateException(
                    "Doctor already has overlapping appointment"
            );
        }

        return scheduleRepository.findForUpdate(
                        doctor.getId(),
                        request.getDate(),
                        request.getStartTime()
                )
                .map(existing -> {

                    if (!existing.isAvailable()) {

                        throw new IllegalStateException(
                                "Appointment slot already booked"
                        );
                    }

                    existing.setAvailable(false);

                    return scheduleRepository.save(existing);
                })
                .orElseGet(() -> {

                    DoctorSchedule schedule =
                            DoctorSchedule.builder()
                                    .doctorProfile(doctor)
                                    .scheduleDate(request.getDate())
                                    .startTime(request.getStartTime())
                                    .endTime(request.getEndTime())
                                    .available(false)
                                    .isLocked(false)
                                    .build();

                    return scheduleRepository.save(schedule);
                });
    }

    private void validateTime(AppointmentRequest request) {

        if (request.getStartTime()
                .isAfter(request.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time cannot be after end time"
            );
        }

        if (request.getStartTime()
                .equals(request.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time and end time cannot be same"
            );
        }
    }

    private Appointment createAppointment(
            AppointmentRequest request,
            DoctorProfile doctor,
            PatientProfile patient,
            DoctorSchedule schedule
    ) {

        String meetingToken = UUID.randomUUID().toString();

        LocalDateTime appointmentDateTime =
                LocalDateTime.of(
                        request.getDate(),
                        request.getStartTime()
                );

        return appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor)
                        .patient(patient)
                        .schedule(schedule)
                        .appointmentRequest(request)
                        .appointmentDate(appointmentDateTime)
                        .meetingToken(meetingToken)
                        .status(AppointmentStatus.SCHEDULED)
                        .checkupType(DoctorCheckType.GENERAL)
                        .build()
        );
    }

    private String buildMeetingLink(Appointment appointment) {

        return meetingBaseUrl
                + "/meet/"
                + appointment.getId()
                + "?token="
                + appointment.getMeetingToken();
    }

    private void notifyBothParties(
            DoctorProfile doctor,
            PatientProfile patient,
            Appointment appointment,
            String meetingLink
    ) {

        String subject = "Appointment Scheduled";

        String body =
                "Your appointment with Dr. "
                        + doctor.getFullName()
                        + " is scheduled for "
                        + appointment.getAppointmentDate()
                        + ".\n\nJoin Meeting:\n"
                        + meetingLink;

        emailService.sendEmail(
                patient.getEmail(),
                subject,
                body
        );

        emailService.sendEmail(
                doctor.getEmail(),
                subject,
                body
        );
    }
}