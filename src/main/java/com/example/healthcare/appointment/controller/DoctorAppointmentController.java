package com.example.healthcare.appointment.controller;

import com.example.healthcare.appointment.dto.response.*;
import com.example.healthcare.appointment.dto.response.DailyAppointmentCountDto;
import com.example.healthcare.appointment.service.DoctorAppointmentService;
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
@RequestMapping("/api/appointments/doctor")
@RequiredArgsConstructor
public class DoctorAppointmentController {

    private final DoctorAppointmentService doctorAppointmentService;
    private final AppointmentStrategyRegistry strategyRegistry;

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<DoctorAppointmentDto>>> getUpcoming(
            @RequestHeader("X-User-Id") UUID doctorId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Upcoming appointments fetched successfully",
                doctorAppointmentService.getUpcoming(doctorId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DoctorAppointmentDto>>> getAll(
            @RequestHeader("X-User-Id") UUID doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Appointments fetched successfully",
                doctorAppointmentService.getAll(doctorId, page, size)));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<DoctorAppointmentDto>>> getHistory(
            @RequestHeader("X-User-Id") UUID doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Appointment history fetched successfully",
                doctorAppointmentService.getHistory(doctorId, page, size)));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<DailyAppointmentCountDto>>> getByRange(
            @RequestHeader("X-User-Id") UUID doctorId,
            @RequestParam String range) {
        AppointmentRangeStrategy strategy = strategyRegistry.get(range);
        return ResponseEntity.ok(new ApiResponse<>(true,
                range + " appointments fetched successfully",
                doctorAppointmentService.getByRange(doctorId, strategy)));
    }


    @GetMapping("/checkup-count")
    public ResponseEntity<ApiResponse<List<CheckupTypeCountDto>>> getCheckupCount(
            @RequestHeader("X-User-Id") UUID doctorId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Checkup counts fetched successfully",
                doctorAppointmentService.getCheckupCount(doctorId)));
    }
}