package com.example.healthcare.dto;

import com.example.healthcare.enums.DoctorCheckType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckupTypeCountDto {
    private DoctorCheckType checkupType;
    private Long count;
}

