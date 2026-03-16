package com.example.healthcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponseDto {
    private Long id;
    private String name;
    private String description;
    private String dosage;
    private String category;
    private String sideEffects;
    private String manufacturer;
    private UUID addedByDoctorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
