// appointment/controller/PatientAppointmentController.java
package com.example.healthcare.appointment.controller;

import com.example.healthcare.appointment.dto.response.*;
import com.example.healthcare.appointment.service.PatientAppointmentService;
import com.example.healthcare.common.config.AppointmentStrategyRegistry;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.common.dto.PageResponse;
import com.example.healthcare.common.service.strategy.AppointmentRangeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments/patient")
@RequiredArgsConstructor
public class PatientAppointmentController {

    private final PatientAppointmentService patientAppointmentService;
    private final AppointmentStrategyRegistry strategyRegistry;

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<PatientAppointmentDto>>> getUpcoming(
            @RequestHeader("X-User-Id") UUID patientId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Upcoming appointments fetched successfully",
                patientAppointmentService.getUpcoming(patientId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PatientAppointmentDto>>> getAll(
            @RequestHeader("X-User-Id") UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Appointments fetched successfully",
                patientAppointmentService.getAll(patientId, page, size)));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<PatientAppointmentDto>>> getHistory(
            @RequestHeader("X-User-Id") UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Appointment history fetched successfully",
                patientAppointmentService.getHistory(patientId, page, size)));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<DailyAppointmentCountDto>>> getByRange(
            @RequestHeader("X-User-Id") UUID patientId,
            @RequestParam String range) {
        AppointmentRangeStrategy strategy = strategyRegistry.get(range);
        return ResponseEntity.ok(new ApiResponse<>(true,
                range + " appointments fetched successfully",
                patientAppointmentService.getByRange(patientId, strategy)));
    }

}