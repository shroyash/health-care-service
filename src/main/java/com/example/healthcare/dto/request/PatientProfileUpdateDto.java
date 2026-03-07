package com.example.healthcare.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PatientProfileUpdateDto {
    private String fullname;
    private String contactNumber;
}
