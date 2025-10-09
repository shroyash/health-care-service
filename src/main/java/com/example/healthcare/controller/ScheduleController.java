package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // ----------------- Save Weekly Schedule -----------------
    @PostMapping("/weekly")
    public ResponseEntity<ApiResponse<Void>> saveWeeklySchedule(
            @Valid @RequestBody DoctorScheduleDto dto) {

        scheduleService.saveWeeklySchedule(dto);

        ApiResponse<Void> response = new ApiResponse<>(true, "Doctor schedule created successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
