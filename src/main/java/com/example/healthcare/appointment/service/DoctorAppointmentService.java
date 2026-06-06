
package com.example.healthcare.appointment.service;

import com.example.healthcare.appointment.dto.response.*;
import com.example.healthcare.appointment.dto.response.DailyAppointmentCountDto;
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
public class DoctorAppointmentService {

    private final AppointmentRepository appointmentRepository;


    @Transactional
    public List<DoctorAppointmentDto> getUpcoming(UUID doctorId) {
        appointmentRepository.cancelExpiredForDoctor(doctorId, LocalDateTime.now());
        return appointmentRepository
                .findUpcomingByDoctor(doctorId, LocalDateTime.now());
    }


    @Transactional
    public PageResponse<DoctorAppointmentDto> getAll(UUID doctorId, int page, int size) {
        appointmentRepository.cancelExpiredForDoctor(doctorId, LocalDateTime.now());
        return PageResponse.of(
                appointmentRepository.findAllByDoctor(
                        doctorId,
                        PageRequest.of(page, size,
                                Sort.by("appointmentDate").descending())));
    }


    @Transactional(readOnly = true)
    public PageResponse<DoctorAppointmentDto> getHistory(UUID doctorId, int page, int size) {
        return PageResponse.of(
                appointmentRepository.findHistoryByDoctor(
                        doctorId,
                        PageRequest.of(page, size,
                                Sort.by("appointmentDate").descending())));
    }


    @Transactional(readOnly = true)
    public List<DailyAppointmentCountDto> getByRange(
            UUID doctorId, AppointmentRangeStrategy strategy) {
        return appointmentRepository.countByDoctorAndRange(
                doctorId,
                strategy.getStartDateTime(),
                strategy.getEndDateTime());
    }


    @Transactional(readOnly = true)
    public List<CheckupTypeCountDto> getCheckupCount(UUID doctorId) {
        return appointmentRepository.countByCheckupType(doctorId);
    }
}