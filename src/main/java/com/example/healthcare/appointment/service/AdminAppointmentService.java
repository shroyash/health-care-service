
package com.example.healthcare.appointment.service;

import com.example.healthcare.appointment.dto.response.AppointmentFullDto;
import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAppointmentService {

    private final AppointmentRepository appointmentRepository;

    // paginated — admin views all appointments across all users
    @Transactional(readOnly = true)
    public PageResponse<AppointmentFullDto> getRecentAppointments(int page, int size) {
        return PageResponse.of(
                appointmentRepository.findAllWithDetails(
                        PageRequest.of(page, size,
                                Sort.by("appointmentDate").descending())));
    }
}