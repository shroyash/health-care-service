package com.example.healthcare.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorWithScheduleDto {

    private UUID doctorProfileId;
    private String name;
    private String profileUrl;
    private String specialty;
    private String email;
    private String phone;

    private List<ScheduleDto> schedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleDto {
        private String date;
        private String startTime;
        private String endTime;
        private boolean available;
    }
}
