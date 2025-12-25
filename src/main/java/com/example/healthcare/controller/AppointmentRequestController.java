package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.AppointmentRequestDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.Appointment;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.service.AppointmentRequestService;
import com.example.healthcare.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRequestController {

    private final AppointmentRequestService service;
    private final AppointmentRepository appointmentRepository;

    public AppointmentRequestController(AppointmentRequestService service, AppointmentRepository appointmentRepository) {
        this.service = service;
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<String>> createRequest(
            @Valid @RequestBody AppointmentRequestDto requestDto,
            @CookieValue(name = "jwt", required = true) String token) {

        service.createRequest(requestDto, token);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(true)
                .message("Appointment request created successfully")
                .data(null)
                .build());
    }

    @GetMapping("/doctor")
    public ResponseEntity<ApiResponse<List<AppointmentRequestDto>>> getRequestsForDoctor(
            @CookieValue(name = "jwt", required = true) String token) {

        long userId = JwtUtils.extractUserIdFromToken(token);
        List<AppointmentRequestDto> requests = service.getRequestsForDoctor(userId);

        return ResponseEntity.ok(ApiResponse.<List<AppointmentRequestDto>>builder()
                .status(true)
                .message("Fetched appointment requests for doctor")
                .data(requests)
                .build());
    }

    @GetMapping("/patient")
    public ResponseEntity<ApiResponse<List<AppointmentRequestDto>>> getRequestsForPatient(
            @CookieValue(name = "jwt", required = true) String token) {

        List<AppointmentRequestDto> requests = service.getRequestsForPatient(token);

        return ResponseEntity.ok(ApiResponse.<List<AppointmentRequestDto>>builder()
                .status(true)
                .message("Fetched your appointment requests")
                .data(requests)
                .build());
    }

    @PatchMapping("/update-status/{requestId}")
    public ResponseEntity<ApiResponse<AppointmentRequestDto>> updateStatus(
            @PathVariable Long requestId,
            @RequestParam String status,
            @CookieValue(name = "jwt", required = true) String token) {

        AppointmentRequestDto updatedRequest = service.updateStatus(requestId, status, token);

        return ResponseEntity.ok(ApiResponse.<AppointmentRequestDto>builder()
                .status(true)
                .message("Appointment status updated successfully")
                .data(updatedRequest)
                .build());
    }

    @GetMapping("/appointments/{id}/access")
    public ResponseEntity<?> validateMeetingToken(
            @PathVariable Long id,
            @RequestParam String token  // this is the meeting token in the URL
    ) {
        // Fetch the appointment
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Validate meeting token
        if (!appointment.getMeetingToken().equals(token)) {
            return ResponseEntity.status(403).body("Invalid meeting token");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentTime = appointment.getAppointmentDate();

//        boolean canJoin = now.isAfter(appointmentTime.minusMinutes(5));
          boolean canJoin = true;

        // Return necessary details for frontend
        return ResponseEntity.ok(Map.of(
                "appointmentId", appointment.getId(),
                "doctorName", appointment.getDoctor().getFullName(),
                "patientName", appointment.getPatient().getFullName(),
                "appointmentTime", appointmentTime.toString(),
                "canJoin", canJoin
        ));
    }


}
