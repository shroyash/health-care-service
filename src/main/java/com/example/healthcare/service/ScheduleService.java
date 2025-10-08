package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorScheduleDto;

public interface ScheduleService {
    void saveWeeklySchedule(DoctorScheduleDto dto);
}
