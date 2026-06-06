package com.example.healthcare.common.service.strategy;


import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component("weekly")
public class WeeklyAppointmentStrategy implements AppointmentRangeStrategy {

    @Override
    public LocalDateTime getStartDateTime() {
        return LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
    }

    @Override
    public LocalDateTime getEndDateTime() {
        return LocalDate.now().with(DayOfWeek.MONDAY).plusDays(6).atTime(23, 59, 59);
    }
}