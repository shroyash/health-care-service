package com.example.healthcare.service;

import com.example.healthcare.dto.request.DoctorScheduleDto;
import com.example.healthcare.dto.response.DoctorScheduleResponseDto;
import com.example.healthcare.dto.response.DoctorScheduleUpdateDTO;
import com.example.healthcare.model.DoctorSchedule;

import java.util.UUID;

public interface ScheduleService {
    void saveWeeklySchedule(DoctorScheduleDto dto,UUID doctorProfileId);
    DoctorScheduleResponseDto getDoctorScheduleWithDetails(UUID doctorProfileId);
    void deleteSchedule(long scheduleId);
    DoctorSchedule updateSchedule(Long id, DoctorScheduleUpdateDTO dto);
}





