
package com.example.healthcare.dashboard.service.imp;

import com.example.healthcare.appointment.repository.AppointmentRepository;
import com.example.healthcare.common.enums.Status;
import com.example.healthcare.dashboard.dto.PatientDashboardStatsDto;
import com.example.healthcare.dashboard.service.PatientDashboardService;
import com.example.healthcare.doctor.repository.DoctorProfileRepository;
import com.example.healthcare.patient.repository.PatientProfileRepository;
import com.example.healthcare.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisSubscribedConnectionException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl implements PatientDashboardService {

    // Facade — coordinates 4 domains
    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final ReportRepository reportRepository;

    @Override
    public PatientDashboardStatsDto getDashboardStats(UUID patientId) {
        if (!patientProfileRepository.existsById(patientId)) {
            throw new RedisSubscribedConnectionException("Patient profile not found");
        }

        long totalUpcoming = appointmentRepository
                .countUpcomingByPatient(patientId);

        long totalActiveDoctors = doctorProfileRepository.
         countByStatus(Status.ACTIVE);

        long totalReports = reportRepository
                .countByPatientId(patientId);

        return new PatientDashboardStatsDto(
                totalUpcoming,
                totalActiveDoctors,
                totalReports);
    }
}