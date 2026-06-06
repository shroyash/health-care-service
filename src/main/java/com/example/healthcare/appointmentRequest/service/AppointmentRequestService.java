// appointmentrequest/service/Imp/AppointmentRequestServiceImpl.java
package com.example.healthcare.appointmentRequest.service;

import com.example.healthcare.appointment.service.AppointmentApprovalService;
import com.example.healthcare.appointmentRequest.dto.request.AppointmentRequestDto;
import com.example.healthcare.appointmentRequest.dto.response.AppointmentRequestResponseDto;
import com.example.healthcare.appointmentRequest.enums.AppointmentRequestStatus;
import com.example.healthcare.appointmentRequest.mapper.AppointmentRequestMapper;
import com.example.healthcare.appointmentRequest.model.AppointmentRequest;
import com.example.healthcare.appointmentRequest.repository.AppointmentRequestRepository;
import com.example.healthcare.appointmentRequest.service.state.AppointmentStatusTransition;
import com.example.healthcare.common.exceptions.DuplicateRequestException;
import com.example.healthcare.common.exceptions.ResourceNotFoundException;
import com.example.healthcare.common.exceptions.UnauthorizedException;
import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.repository.DoctorProfileRepository;
import com.example.healthcare.patient.model.PatientProfile;
import com.example.healthcare.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentRequestService {

    private final AppointmentRequestRepository requestRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentApprovalService approvalService;
    private final AppointmentStatusTransition statusTransition;
    private final AppointmentRequestMapper appointmentRequestMapper;


    @Transactional
    public void createRequest(AppointmentRequestDto dto, UUID patientId, String username) {
        PatientProfile patient = findPatient(patientId);

        if (requestRepository.existsByPatientIdAndDoctorIdAndDateAndStartTime(
                patient.getId(), dto.getDoctorId(),
                dto.getDate(), dto.getStartTime())) {
            throw new DuplicateRequestException(
                    "You already requested this slot. " +
                            "Please wait for the doctor to respond.");
        }

        AppointmentRequest request = AppointmentRequest.builder()
                .patientId(patient.getId())
                .patientFullName(username)
                .doctorId(dto.getDoctorId())
                .doctorFullName(dto.getDoctorName())
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(AppointmentRequestStatus.PENDING)
                .notes(dto.getNotes())
                .build();

        requestRepository.save(request);

    }


    public List<AppointmentRequestResponseDto> getRequestsForDoctor(UUID doctorId) {
        DoctorProfile doctor = findDoctor(doctorId);
        return requestRepository.findByDoctorId(doctor.getId())
                .stream()
                .map(appointmentRequestMapper::toDto)
                .toList();
    }


    public List<AppointmentRequestResponseDto> getRequestsForPatient(UUID patientId) {
        PatientProfile patient = findPatient(patientId);
        return requestRepository.findByPatientId(patient.getId())
                .stream()
                .map(appointmentRequestMapper::toDto)
                .toList();
    }


    @Transactional
    public AppointmentRequestResponseDto updateStatus(Long requestId,
                                                      String status,
                                                      UUID doctorId) {
        DoctorProfile doctor = findDoctor(doctorId);

        AppointmentRequest request = requestRepository.findByIdForUpdate(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getDoctorId().equals(doctor.getId())) {
            throw new UnauthorizedException("Unauthorized to update this request");
        }

        AppointmentRequestStatus newStatus =
                AppointmentRequestStatus.valueOf(status.toUpperCase());

        statusTransition.validate(request.getStatus(), newStatus);

        request.setStatus(newStatus);
        requestRepository.save(request);

        if (newStatus == AppointmentRequestStatus.APPROVED) {
            PatientProfile patient = findPatient(request.getPatientId());
            approvalService.approve(request, doctor, patient);
        }

        return appointmentRequestMapper.toDto(request);
    }

    // ── private helpers ──────────────────────────────────────────

    private PatientProfile findPatient(UUID id) {
        return patientProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient profile not found"));
    }

    private DoctorProfile findDoctor(UUID id) {
        return doctorProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));
    }
}