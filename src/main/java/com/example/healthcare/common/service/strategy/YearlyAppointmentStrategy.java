package com.example.healthcare.common.service.strategy;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Component("yearly")
public class YearlyAppointmentStrategy implements AppointmentRangeStrategy {

    @Override
    public LocalDateTime getStartDateTime() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
    }

    @Override
    public LocalDateTime getEndDateTime() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59);
    }
}
