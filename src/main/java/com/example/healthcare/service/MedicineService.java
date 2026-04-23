package com.example.healthcare.service;

import com.example.healthcare.dto.request.MedicineRequestDto;
import com.example.healthcare.dto.response.MedicineResponseDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.exceptions.UnauthorizedException;
import com.example.healthcare.model.Medicine;
import com.example.healthcare.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Transactional
    public MedicineResponseDto addMedicine(MedicineRequestDto dto, UUID doctorId) {
        Medicine medicine = Medicine.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .dosage(dto.getDosage())
                .category(dto.getCategory())
                .sideEffects(dto.getSideEffects())
                .manufacturer(dto.getManufacturer())
                .addedByDoctorId(doctorId)
                .build();

        return toDto(medicineRepository.save(medicine));
    }

    @Transactional(readOnly = true)
    public List<MedicineResponseDto> getAllMedicines() {
        return medicineRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicineResponseDto getMedicineById(Long id) {
        return toDto(findMedicineOrThrow(id));
    }

    @Transactional
    public MedicineResponseDto updateMedicine(
            Long id,
            MedicineRequestDto dto,
            UUID doctorId,
            boolean isAdmin) {

        Medicine medicine = findMedicineOrThrow(id);

        if (!isAdmin && !medicine.getAddedByDoctorId().equals(doctorId)) {
            throw new UnauthorizedException("You can only edit your own medicines");
        }

        medicine.setName(dto.getName());
        medicine.setDescription(dto.getDescription());
        medicine.setDosage(dto.getDosage());
        medicine.setCategory(dto.getCategory());
        medicine.setSideEffects(dto.getSideEffects());
        medicine.setManufacturer(dto.getManufacturer());

        return toDto(medicineRepository.save(medicine));
    }

    @Transactional
    public void deleteMedicine(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new UnauthorizedException("Only admin can delete medicines");
        }

        Medicine medicine = findMedicineOrThrow(id);
        medicineRepository.delete(medicine);
    }

    // ── private helpers ──────────────────────────────────────────

    private Medicine findMedicineOrThrow(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
    }

    private MedicineResponseDto toDto(Medicine m) {
        return MedicineResponseDto.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .dosage(m.getDosage())
                .category(m.getCategory())
                .sideEffects(m.getSideEffects())
                .manufacturer(m.getManufacturer())
                .addedByDoctorId(m.getAddedByDoctorId())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}