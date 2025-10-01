package com.example.healthcare.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DoctorProfileCreateDto {
    private long doctorId;
    private String userName;
    private String email;
}
