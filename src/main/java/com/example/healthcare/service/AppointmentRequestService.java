package com.example.healthcare.service;

import com.example.healthcare.dto.AppointmentDoctorReceiveResponseDto;
import com.example.healthcare.dto.AppointmentPatientRequestResponseDto;
import com.example.healthcare.dto.AppointmentRequestDto;

import java.util.List;

public interface AppointmentRequestService {

    void createRequest(AppointmentRequestDto dto, String token);

    List<AppointmentRequestDto> getRequestsForDoctor(Long doctorId);

    List<AppointmentRequestDto> getRequestsForPatient(String token);

    AppointmentRequestDto updateStatus(Long requestId, String status, String token);
}
