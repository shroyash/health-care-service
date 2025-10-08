package com.example.healthcare.controller;

import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/weekly")
    public ResponseEntity<String> saveWeeklySchedule(@RequestBody DoctorScheduleDto dto) {
        scheduleService.saveWeeklySchedule(dto);
        return ResponseEntity.ok("Weekly schedule saved successfully");
    }
}
