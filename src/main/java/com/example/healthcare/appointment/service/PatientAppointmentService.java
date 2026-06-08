
package com.example.healthcare.appointment.service;

import com.example.healthcare.appointment.dto.response.*;
import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.common.dto.PageResponse;
import com.example.healthcare.common.service.strategy.AppointmentRangeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientAppointmentService {

    private final AppointmentRepository appointmentRepository;


    @Transactional
    public List<PatientAppointmentDto> getUpcoming(UUID patientId) {
        appointmentRepository.cancelExpiredForPatient(patientId, LocalDateTime.now());
        return appointmentRepository
                .findUpcomingByPatient(patientId, LocalDateTime.now());
    }


    @Transactional(readOnly = true)
    public PageResponse<PatientAppointmentDto> getAll(UUID patientId, int page, int size) {
        return PageResponse.of(
                appointmentRepository.findAllByPatient(
                        patientId,
                        PageRequest.of(page, size,
                                Sort.by("appointmentDate").descending())));
    }


    @Transactional(readOnly = true)
    public PageResponse<PatientAppointmentDto> getHistory(UUID patientId, int page, int size) {
        return PageResponse.of(
                appointmentRepository.findHistoryByPatient(
                        patientId,
                        PageRequest.of(page, size,
                                Sort.by("appointmentDate").descending())));
    }


    @Transactional(readOnly = true)
    public List<DailyAppointmentCountDto> getByRange(
            UUID patientId, AppointmentRangeStrategy strategy) {
        return appointmentRepository.countByPatientAndRange(
                patientId,
                strategy.getStartDateTime(),
                strategy.getEndDateTime());
    }

    public List<StatusCountResponseDto> getAppointmentStatusCount(UUID patientId) {
        return appointmentRepository.getAppointmentStatusCountByPatient(patientId);
    }


}