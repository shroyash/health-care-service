package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.dto.DoctorScheduleResponseDto;
import com.example.healthcare.dto.DoctorScheduleUpdateDTO;
import com.example.healthcare.model.DoctorSchedule;

import java.util.List;
import java.util.UUID;

public interface ScheduleService {
    void saveWeeklySchedule(DoctorScheduleDto dto,UUID doctorProfileId);
    DoctorScheduleResponseDto getDoctorScheduleWithDetails(UUID doctorProfileId);
    void deleteSchedule(long scheduleId);
    DoctorSchedule updateSchedule(Long id, DoctorScheduleUpdateDTO dto);
}





