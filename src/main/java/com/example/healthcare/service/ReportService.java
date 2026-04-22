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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileService doctorProfileService;
    private final PatientProfileService patientProfileService;

    @Transactional
    public ReportResponseDto createReport(ReportRequestDto request, UUID doctorId) {  // ✅ UUID not token
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
                .build();  // ✅ remove manual createdAt/updatedAt — use JPA auditing

        medicines.forEach(m -> m.setMedicalReport(report));

        MedicalReport saved = reportRepository.save(report);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return mapToResponseDto(saved);
    }

    @Transactional
    public ReportResponseDto updateReport(Long reportId, ReportRequestDto request, UUID doctorId) {  // ✅
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

        report.getMedicines().clear();
        report.getMedicines().addAll(mapToMedicineEntities(request.getMedicines()));
        report.getMedicines().forEach(m -> m.setMedicalReport(report));

        return mapToResponseDto(reportRepository.save(report));
    }

    @Transactional
    public ReportResponseDto finalizeReport(Long reportId, UUID doctorId) {  // ✅
        MedicalReport report = getReportOrThrow(reportId);

        if (!report.getDoctorId().equals(doctorId)) {
            throw new UnauthorizedException("You are not authorized to finalize this report");
        }

        if (report.getStatus() == ReportStatus.FINALIZED) {
            throw new IllegalStateException("Report is already finalized");
        }

        report.setStatus(ReportStatus.FINALIZED);
        report.setFinalizedAt(LocalDateTime.now());

        return mapToResponseDto(reportRepository.save(report));
    }

    @Transactional(readOnly = true)
    public List<ReportResponseDto> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReportResponseDto getReportById(Long reportId) {
        return mapToResponseDto(getReportOrThrow(reportId));
    }

    @Transactional(readOnly = true)
    public ReportResponseDto getReportByAppointmentId(Long appointmentId) {
        MedicalReport report = reportRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for this appointment"));
        return mapToResponseDto(report);
    }

    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByPatient(UUID patientId) {
        return reportRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByDoctor(UUID doctorId) {
        return reportRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ── private helpers ──────────────────────────────────────────

    private MedicalReport getReportOrThrow(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    private List<ReportMedicine> mapToMedicineEntities(List<ReportMedicineDto> dtos) {
        return dtos.stream()
                .map(dto -> ReportMedicine.builder()
                        .name(dto.getName())
                        .dosage(dto.getDosage())
                        .frequency(dto.getFrequency())
                        .duration(dto.getDuration())
                        .instructions(dto.getInstructions())
                        .build())  // ✅ remove manual createdAt
                .collect(Collectors.toList());
    }

    private ReportResponseDto mapToResponseDto(MedicalReport report) {
        String patientName = patientProfileService
                .getPatientProfile(report.getPatientId()).getFullName();
        String doctorName = doctorProfileService
                .getDoctorProfile(report.getDoctorId()).getFullName();

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
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        dto.setMedicines(report.getMedicines().stream()
                .map(this::toMedicineDto)  // ✅ extracted
                .collect(Collectors.toList()));
        return dto;
    }

    private ReportMedicineDto toMedicineDto(ReportMedicine m) {
        ReportMedicineDto dto = new ReportMedicineDto();
        dto.setName(m.getName());
        dto.setDosage(m.getDosage());
        dto.setFrequency(m.getFrequency());
        dto.setDuration(m.getDuration());
        dto.setInstructions(m.getInstructions());
        return dto;
    }
}