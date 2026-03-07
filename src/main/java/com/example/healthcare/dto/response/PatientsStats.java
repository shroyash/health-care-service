package com.example.healthcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientsStats {
    long activePatients;
    long totalPatients;
    long totalAppointments;
}
