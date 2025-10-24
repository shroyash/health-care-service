package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.dto.DoctorScheduleResponseDto;
import com.example.healthcare.model.DoctorSchedule;

import java.util.List;

public interface ScheduleService {
    void saveWeeklySchedule(DoctorScheduleDto dto);
    DoctorScheduleResponseDto getDoctorScheduleWithDetails(Long doctorProfileId);
}





