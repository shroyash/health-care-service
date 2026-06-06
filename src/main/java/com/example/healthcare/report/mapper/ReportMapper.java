package com.example.healthcare.report.mapper;

import com.example.healthcare.report.dto.ReportMedicineDto;
import com.example.healthcare.report.dto.ReportResponseDto;
import com.example.healthcare.report.model.MedicalReport;
import com.example.healthcare.report.model.ReportMedicine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportMapper {

    public ReportResponseDto toDto(MedicalReport report, String patientName, String doctorName) {
        ReportResponseDto dto = new ReportResponseDto();
        dto.setId(report.getId());
        dto.setAppointmentId(report.getAppointment().getId());
        dto.setPatientId(report.getPatientId());
        dto.setDoctorId(report.getDoctorId());
        dto.setPatientName(patientName);
        dto.setDoctorName(doctorName);
        dto.setTitle(report.getTitle());
        dto.setDiagnosis(report.getDiagnosis());
        dto.setSymptoms(report.getSymptoms());
        dto.setTreatmentPlan(report.getTreatmentPlan());
        dto.setNotes(report.getNotes());
        dto.setReportType(report.getReportType());
        dto.setStatus(report.getStatus());
        dto.setReportUrl(report.getReportUrl());
        dto.setFinalizedAt(report.getFinalizedAt());
        dto.setMedicines(report.getMedicines().stream()
                .map(this::toMedicineDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public ReportMedicineDto toMedicineDto(ReportMedicine m) {
        ReportMedicineDto dto = new ReportMedicineDto();
        dto.setName(m.getName());
        dto.setDosage(m.getDosage());
        dto.setFrequency(m.getFrequency());
        dto.setDuration(m.getDuration());
        dto.setInstructions(m.getInstructions());
        return dto;
    }

    public List<ReportMedicine> toMedicineEntities(List<ReportMedicineDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(dto -> ReportMedicine.builder()
                        .name(dto.getName())
                        .dosage(dto.getDosage())
                        .frequency(dto.getFrequency())
                        .duration(dto.getDuration())
                        .instructions(dto.getInstructions())
                        .build())
                .collect(Collectors.toList());
    }
}
