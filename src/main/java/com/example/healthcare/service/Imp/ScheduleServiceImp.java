package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.exceptions.DoctorProfileNotFoundException;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.model.DoctorSchedule;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImp {

    private final DoctorProfileRepository doctorProfileRepository;
    private final ScheduleRepository scheduleRepository;

    public void saveWeeklySchedule(DoctorScheduleDto dto) {
        DoctorProfile doctor = doctorProfileRepository.findById(dto.getDoctorProfileId())
                .orElseThrow(() -> new DoctorProfileNotFoundException("Doctor not found"));

        // Remove old schedules if needed (optional)
        doctor.getSchedules().clear();

        // Converting DTO schedules to entity
        List<DoctorSchedule> scheduleList = dto.getSchedules().stream().map(s -> {
            DoctorSchedule schedule = DoctorSchedule.builder()
                    .dayOfWeek(s.getDayOfWeek())
                    .startTime(s.getStartTime())
                    .endTime(s.getEndTime())
                    .available(s.isAvailable())
                    .doctorProfile(doctor)
                    .build();
            return schedule;
        }).toList();

        // Adding schedules to doctor
        doctor.setSchedules(scheduleList);

        // Save doctor along with schedules (cascade = ALL)
        doctorProfileRepository.save(doctor);
    }
}

