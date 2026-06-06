package com.example.healthcare.medicine.controller;

import com.example.healthcare.medicine.dto.MedicineRequestDto;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.medicine.dto.MedicineResponseDto;
import com.example.healthcare.medicine.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicineResponseDto>>> getAllMedicines() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Medicines fetched",
                        medicineService.getAllMedicines())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponseDto>> getMedicine(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Medicine fetched",
                        medicineService.getMedicineById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicineResponseDto>> addMedicine(
            @RequestBody MedicineRequestDto dto,
            @RequestHeader("X-User-Id") UUID doctorId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Medicine added",
                        medicineService.addMedicine(dto, doctorId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponseDto>> updateMedicine(
            @PathVariable Long id,
            @RequestBody MedicineRequestDto dto,
            @RequestHeader("X-User-Id") UUID doctorId,
            @RequestHeader("X-User-Roles") String roles) {

        boolean isAdmin = roles.contains("ROLE_ADMIN");

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Medicine updated",
                        medicineService.updateMedicine(id, dto, doctorId, isAdmin))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(
            @PathVariable Long id,
            @RequestHeader("X-User-Roles") String roles) {

        boolean isAdmin = roles.contains("ROLE_ADMIN");
        medicineService.deleteMedicine(id, isAdmin);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Medicine deleted", null)
        );
    }
}