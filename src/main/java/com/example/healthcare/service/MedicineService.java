package com.example.healthcare.service;

import com.example.healthcare.dto.request.MedicineRequestDto;
import com.example.healthcare.dto.response.MedicineResponseDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.Medicine;
import com.example.healthcare.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineResponseDto addMedicine(MedicineRequestDto dto, UUID doctorId) {
        Medicine medicine = Medicine.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .dosage(dto.getDosage())
                .category(dto.getCategory())
                .sideEffects(dto.getSideEffects())
                .manufacturer(dto.getManufacturer())
                .addedByDoctorId(doctorId)
                .createdAt(LocalDateTime.now())
                .build();

        return toDto(medicineRepository.save(medicine));
    }

    public List<MedicineResponseDto> getAllMedicines() {
        return medicineRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public MedicineResponseDto getMedicineById(Long id) {
        return toDto(medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found")));
    }

    public MedicineResponseDto updateMedicine(Long id, MedicineRequestDto dto, UUID doctorId, boolean isAdmin) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (!isAdmin && !medicine.getAddedByDoctorId().equals(doctorId)) {
            throw new RuntimeException("You can only edit your own medicines");
        }

        medicine.setName(dto.getName());
        medicine.setDescription(dto.getDescription());
        medicine.setDosage(dto.getDosage());
        medicine.setCategory(dto.getCategory());
        medicine.setSideEffects(dto.getSideEffects());
        medicine.setManufacturer(dto.getManufacturer());

        return toDto(medicineRepository.save(medicine));
    }

    public void deleteMedicine(Long id) {
        medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
        medicineRepository.deleteById(id);
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