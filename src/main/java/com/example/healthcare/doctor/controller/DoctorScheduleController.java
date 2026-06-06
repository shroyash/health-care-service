package com.example.healthcare.doctor.controller;

import com.example.healthcare.doctor.service.DoctorScheduleService;
import com.example.healthcare.doctor.dto.DoctorScheduleDto;
import com.example.healthcare.doctor.dto.DoctorScheduleUpdateDTO;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.doctor.dto.DoctorScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

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