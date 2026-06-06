package com.example.healthcare.appointment.controller;

import com.example.healthcare.appointment.dto.response.AppointmentFullDto;
import com.example.healthcare.appointment.service.AdminAppointmentService;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments/admin")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AdminAppointmentService adminAppointmentService;


    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentFullDto>>> getRecent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Recent appointments fetched successfully",
                adminAppointmentService.getRecentAppointments(page, size)));
    }
}