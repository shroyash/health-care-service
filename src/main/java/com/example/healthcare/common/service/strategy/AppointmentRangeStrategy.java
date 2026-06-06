package com.example.healthcare.common.service.strategy;


import java.time.LocalDateTime;

public interface AppointmentRangeStrategy {
    LocalDateTime getStartDateTime();
    LocalDateTime getEndDateTime();
}