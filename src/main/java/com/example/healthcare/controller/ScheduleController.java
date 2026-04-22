package com.example.healthcare.controller;

import com.example.healthcare.dto.request.DoctorScheduleDto;
import com.example.healthcare.dto.request.DoctorScheduleUpdateDTO;
import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.DoctorScheduleResponseDto;
import com.example.healthcare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/weekly")
    public ResponseEntity<ApiResponse<Void>> saveWeeklySchedule(
            @Valid @RequestBody DoctorScheduleDto dto,
            @RequestHeader("X-User-Id") UUID userId) {

        scheduleService.saveWeeklySchedule(dto, userId);

        return new ResponseEntity<>(
                new ApiResponse<>(true, "Doctor schedule created successfully!", null),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getDoctorSchedule(
            @RequestHeader("X-User-Id") UUID userId) {

        DoctorScheduleResponseDto schedules = scheduleService.getDoctorScheduleWithDetails(userId);

        return ResponseEntity.ok(ApiResponse.<DoctorScheduleResponseDto>builder()
                .status(true)
                .message("Doctor schedules fetched successfully")
                .data(schedules)
                .build());
    }

    @GetMapping("/{doctorProfileId}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getDoctorScheduleById(
            @PathVariable UUID doctorProfileId) {

        DoctorScheduleResponseDto schedules = scheduleService.getDoctorScheduleWithDetails(doctorProfileId);

        return ResponseEntity.ok(ApiResponse.<DoctorScheduleResponseDto>builder()
                .status(true)
                .message("Doctor schedules fetched successfully")
                .data(schedules)
                .build());
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
                                                              @PathVariable long scheduleId) {

        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(true)
                .message("Schedule deleted successfully")
                .data(null)
                .build());
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> updateSchedule(
                                                                                   @PathVariable Long scheduleId,
                                                                                   @RequestBody @Valid DoctorScheduleUpdateDTO dto) {

        DoctorScheduleResponseDto updated = scheduleService.updateSchedule(scheduleId, dto);

        return ResponseEntity.ok(ApiResponse.<DoctorScheduleResponseDto>builder()
                .status(true)
                .message("Schedule updated successfully")
                .data(updated)
                .build());
    }
}