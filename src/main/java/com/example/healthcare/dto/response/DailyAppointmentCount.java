package com.example.healthcare.dto.response;

import java.time.LocalDate;

public interface DailyAppointmentCount {

    LocalDate getDate();
    Long getCount();
}
