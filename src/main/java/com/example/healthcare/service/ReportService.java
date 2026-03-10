package com.example.healthcare.service;


import com.example.healthcare.dto.request.ReportMedicineDto;
import com.example.healthcare.dto.request.ReportRequestDto;
import com.example.healthcare.dto.response.ReportResponseDto;
import com.example.healthcare.enums.AppointmentStatus;
import com.example.healthcare.enums.ReportStatus;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.exceptions.UnauthorizedException;
import com.example.healthcare.model.Appointment;
import com.example.healthcare.model.MedicalReport;
import com.example.healthcare.model.ReportMedicine;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.ReportRepository;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final AppointmentRepository appointmentRepository;


    @Transactional
    public ReportResponseDto createReport(ReportRequestDto request, String token) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (reportRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new IllegalStateException("Report already exists for this appointment");
        }

        List<ReportMedicine> medicines = mapToMedicineEntities(request.getMedicines());

        MedicalReport report = MedicalReport.builder()
                .appointment(appointment)
                .patientId(appointment.getPatient().getId())
                .doctorId(doctorId)
                .title(request.getTitle())
                .diagnosis(request.getDiagnosis())
                .symptoms(request.getSymptoms())
                .treatmentPlan(request.getTreatmentPlan())
                .notes(request.getNotes())
                .reportType(request.getReportType())
                .status(ReportStatus.DRAFT)
                .medicines(medicines)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        medicines.forEach(m -> m.setMedicalReport(report));

        MedicalReport saved = reportRepository.save(report);

        // Mark appointment as COMPLETED when doctor creates a report
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return mapToResponseDto(saved);
    }

    // ── Update ────────────────────────────────────────────
    @Transactional
    public ReportResponseDto updateReport(Long reportId, ReportRequestDto request, String token) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);

        MedicalReport report = getReportOrThrow(reportId);

        if (!report.getDoctorId().equals(doctorId)) {
            throw new UnauthorizedException("You are not authorized to update this report");
        }

        if (report.getStatus() == ReportStatus.FINALIZED) {
            throw new IllegalStateException("Finalized report cannot be updated");
        }

        report.setTitle(request.getTitle());
        report.setDiagnosis(request.getDiagnosis());
        report.setSymptoms(request.getSymptoms());
        report.setTreatmentPlan(request.getTreatmentPlan());
        report.setNotes(request.getNotes());
        report.setReportType(request.getReportType());
        report.setUpdatedAt(LocalDateTime.now());

        report.getMedicines().clear();
        report.getMedicines().addAll(mapToMedicineEntities(request.getMedicines()));
        report.getMedicines().forEach(m -> m.setMedicalReport(report));

        return mapToResponseDto(reportRepository.save(report));
    }

    // ── Finalize ──────────────────────────────────────────
    @Transactional
    public ReportResponseDto finalizeReport(Long reportId, String token) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);

        MedicalReport report = getReportOrThrow(reportId);

        if (!report.getDoctorId().equals(doctorId)) {
            throw new UnauthorizedException("You are not authorized to finalize this report");
        }

        if (report.getStatus() == ReportStatus.FINALIZED) {
            throw new IllegalStateException("Report is already finalized");
        }

        report.setStatus(ReportStatus.FINALIZED);
        report.setFinalizedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        return mapToResponseDto(reportRepository.save(report));
    }

    // ── Get All ───────────────────────────────────────────
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ── Get By Id ─────────────────────────────────────────
    public ReportResponseDto getReportById(Long reportId) {
        return mapToResponseDto(getReportOrThrow(reportId));
    }

    // ── Get By Appointment ────────────────────────────────
    public ReportResponseDto getReportByAppointmentId(Long appointmentId) {
        MedicalReport report = reportRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for this appointment"));
        return mapToResponseDto(report);
    }

    // ── Get By Patient ────────────────────────────────────
    public List<ReportResponseDto> getReportsByPatient(UUID patientId) {
        return reportRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ── Get By Doctor ─────────────────────────────────────
    public List<ReportResponseDto> getReportsByDoctor(UUID doctorId) {
        return reportRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ── Private Helpers ───────────────────────────────────
    private MedicalReport getReportOrThrow(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    private List<ReportMedicine> mapToMedicineEntities(List<ReportMedicineDto> dtos) {
        return dtos.stream().map(dto -> ReportMedicine.builder()
                .name(dto.getName())
                .dosage(dto.getDosage())
                .frequency(dto.getFrequency())
                .duration(dto.getDuration())
                .instructions(dto.getInstructions())
                .createdAt(LocalDateTime.now())
                .build()
        ).collect(Collectors.toList());
    }

    private ReportResponseDto mapToResponseDto(MedicalReport report) {
        ReportResponseDto dto = new ReportResponseDto();
        dto.setId(report.getId());
        dto.setAppointmentId(report.getAppointment().getId());
        dto.setPatientId(report.getPatientId());
        dto.setDoctorId(report.getDoctorId());
        dto.setTitle(report.getTitle());
        dto.setDiagnosis(report.getDiagnosis());
        dto.setSymptoms(report.getSymptoms());
        dto.setTreatmentPlan(report.getTreatmentPlan());
        dto.setNotes(report.getNotes());
        dto.setReportType(report.getReportType());
        dto.setStatus(report.getStatus());
        dto.setReportUrl(report.getReportUrl());
        dto.setFinalizedAt(report.getFinalizedAt());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        dto.setMedicines(report.getMedicines().stream().map(m -> {
            ReportMedicineDto medicineDto = new ReportMedicineDto();
            medicineDto.setName(m.getName());
            medicineDto.setDosage(m.getDosage());
            medicineDto.setFrequency(m.getFrequency());
            medicineDto.setDuration(m.getDuration());
            medicineDto.setInstructions(m.getInstructions());
            return medicineDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}