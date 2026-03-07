package com.example.healthcare.service;

import com.example.healthcare.dto.request.AppointmentRequestDto;

import java.util.List;
import java.util.UUID;

public interface AppointmentRequestService {

    void createRequest(AppointmentRequestDto dto, String token);

    List<AppointmentRequestDto> getRequestsForDoctor(UUID doctorId);

    List<AppointmentRequestDto> getRequestsForPatient(String token);

    AppointmentRequestDto updateStatus(Long requestId, String status, String token);
}
