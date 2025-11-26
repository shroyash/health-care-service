package com.example.healthcare.dto;

import lombok.*;

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
        private String dayOfWeek;
        private String startTime;
        private String endTime;
        private boolean available;
    }
}
