package com.example.healthcare.service;

import com.example.healthcare.dto.request.DoctorScheduleDto;
import com.example.healthcare.dto.request.DoctorScheduleUpdateDTO;
import com.example.healthcare.dto.response.DoctorScheduleResponseDto;

import java.util.UUID;

public interface ScheduleService {
    void saveWeeklySchedule(DoctorScheduleDto dto,UUID doctorProfileId);
    DoctorScheduleResponseDto getDoctorScheduleWithDetails(UUID doctorProfileId);
    void deleteSchedule(long scheduleId);
    DoctorScheduleResponseDto updateSchedule(Long id, DoctorScheduleUpdateDTO dto);
}





