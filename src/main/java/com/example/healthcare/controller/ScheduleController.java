package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.dto.DoctorScheduleResponseDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.service.ScheduleService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DoctorProfileRepository doctorProfileRepository;

    @PostMapping("/weekly")
    public ResponseEntity<ApiResponse<Void>> saveWeeklySchedule(
            @Valid @RequestBody DoctorScheduleDto dto,
            @CookieValue(name = "jwt") String token) {

        Long userId = JwtUtils.extractUserIdFromToken(token);


        scheduleService.saveWeeklySchedule(dto, userId);

        ApiResponse<Void> response = new ApiResponse<>(true, "Doctor schedule created successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getDoctorSchedule(
            @CookieValue(name = "jwt") String token) {

        Long userId = JwtUtils.extractUserIdFromToken(token);

        DoctorProfile doctorProfile = doctorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        long doctorProfileId = doctorProfile.getDoctorProfileId();

        DoctorScheduleResponseDto schedules = scheduleService.getDoctorScheduleWithDetails(doctorProfileId);

        ApiResponse<DoctorScheduleResponseDto> response = ApiResponse.<DoctorScheduleResponseDto>builder()
                .status(true)
                .message("Doctor schedules fetched successfully")
                .data(schedules)

                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{doctorProfileId}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponseDto>> getDoctorScheduleById(
            @PathVariable Long doctorProfileId) {

        DoctorScheduleResponseDto schedules = scheduleService.getDoctorScheduleWithDetails(doctorProfileId);

        ApiResponse<DoctorScheduleResponseDto> response = ApiResponse.<DoctorScheduleResponseDto>builder()
                .status(true)
                .message("Doctor schedules fetched successfully")
                .data(schedules)
                .build();

        return ResponseEntity.ok(response);
    }


}