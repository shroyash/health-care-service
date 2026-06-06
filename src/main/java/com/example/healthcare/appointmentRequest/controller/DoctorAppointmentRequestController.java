
package com.example.healthcare.appointmentRequest.controller;


import com.example.healthcare.appointmentRequest.dto.response.AppointmentRequestResponseDto;
import com.example.healthcare.appointmentRequest.service.AppointmentRequestService;
import com.example.healthcare.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointment-requests/doctor")
@RequiredArgsConstructor
public class DoctorAppointmentRequestController {

    private final AppointmentRequestService appointmentRequestService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentRequestResponseDto>>> getIncomingRequests(
            @RequestHeader("X-User-Id") UUID doctorId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Incoming appointment requests fetched",
                appointmentRequestService.getRequestsForDoctor(doctorId)));
    }

    @PatchMapping("/{requestId}/status")
    public ResponseEntity<ApiResponse<AppointmentRequestResponseDto>> updateStatus(
            @PathVariable Long requestId,
            @RequestParam String status,
            @RequestHeader("X-User-Id") UUID doctorId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Request status updated successfully",
                appointmentRequestService.updateStatus(requestId, status, doctorId)));
    }
}