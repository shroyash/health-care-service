package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.model.DoctorSchedule;
import com.example.healthcare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    
    @PostMapping("/weekly")
    public ResponseEntity<ApiResponse<Void>> saveWeeklySchedule(
            @Valid @RequestBody DoctorScheduleDto dto) {

        scheduleService.saveWeeklySchedule(dto);

        ApiResponse<Void> response = new ApiResponse<>(true, "Doctor schedule created successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorSchedule>>> getDoctorSchedule(
            @Param("doctorProfileId") Long doctorProfileId) {

        List<DoctorSchedule> schedules = scheduleService.getDoctorSchedule(doctorProfileId);

        ApiResponse<List<DoctorSchedule>> response = ApiResponse.<List<DoctorSchedule>>builder()
                .status(true)
                .message("Doctor schedules fetched successfully")
                .data(schedules)
                .build();

        return ResponseEntity.ok(response);
    }

}

