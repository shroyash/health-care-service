package com.example.healthcare.common.service.strategy;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Component("monthly")
public class MonthlyAppointmentStrategy implements AppointmentRangeStrategy {

    @Override
    public LocalDateTime getStartDateTime() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    }

    @Override
    public LocalDateTime getEndDateTime() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
    }
}