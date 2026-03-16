package com.example.healthcare.controller;

import com.example.healthcare.dto.request.MedicineRequestDto;
import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.MedicineResponseDto;
import com.example.healthcare.service.MedicineService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    // All roles can view
    @GetMapping
    public ApiResponse<List<MedicineResponseDto>> getAllMedicines() {
        return new ApiResponse<>(true, "Medicines fetched", medicineService.getAllMedicines());
    }

    @GetMapping("/{id}")
    public ApiResponse<MedicineResponseDto> getMedicine(@PathVariable Long id) {
        return new ApiResponse<>(true, "Medicine fetched", medicineService.getMedicineById(id));
    }

    // Doctor and Admin can add
    @PostMapping
    public ApiResponse<MedicineResponseDto> addMedicine(
            @RequestBody MedicineRequestDto dto,
            @CookieValue("jwt") String token
    ) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);
        return new ApiResponse<>(true, "Medicine added", medicineService.addMedicine(dto, doctorId));
    }

    // Doctor can edit their own, Admin can edit all
    @PutMapping("/{id}")
    public ApiResponse<MedicineResponseDto> updateMedicine(
            @PathVariable Long id,
            @RequestBody MedicineRequestDto dto,
            @CookieValue("jwt") String token
    ) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);
        List<String> roles = JwtUtils.extractRolesFromToken(token);
        boolean isAdmin = roles.contains("ROLE_ADMIN");
        return new ApiResponse<>(true, "Medicine updated", medicineService.updateMedicine(id, dto, doctorId, isAdmin));
    }

    // Only Admin can delete
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMedicine(
            @PathVariable Long id,
            @CookieValue("jwt") String token
    ) {
        List<String> roles = JwtUtils.extractRolesFromToken(token);
        if (!roles.contains("ROLE_ADMIN")) {
            throw new RuntimeException("Only admin can delete medicines");
        }
        medicineService.deleteMedicine(id);
        return new ApiResponse<>(true, "Medicine deleted", null);
    }
}