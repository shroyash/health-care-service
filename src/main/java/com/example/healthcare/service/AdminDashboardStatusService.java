package com.example.healthcare.service;

import com.example.healthcare.dto.AppointmentFullDto;
import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.PatientProfileDTO;
import com.example.healthcare.model.DoctorProfile;

import java.util.List;

public interface AdminDashboardStatusService {

    // Total appointments today
    long getTotalAppointmentsToday();

    // Total number of doctors
    long getTotalDoctors();

    // Total number of patients
    long getTotalPatients();

    // Number of doctors pending approval
    long getPendingDoctorApprovals();

    List<AppointmentFullDto> getRecentAppointments();

}
