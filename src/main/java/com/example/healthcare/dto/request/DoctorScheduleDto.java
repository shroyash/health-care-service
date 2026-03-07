package com.example.healthcare.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorScheduleDto {

    private List<ScheduleDto> schedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleDto {
        private LocalDate scheduleDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private boolean available;
    }
}
