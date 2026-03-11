package com.example.healthcare.service;

import com.example.healthcare.dto.response.AppointmentFullDto;
import com.example.healthcare.dto.response.GenderCountResponseDto;
import com.example.healthcare.dto.response.PatientsStats;
import com.example.healthcare.dto.response.WeeklyAppointmentCountDto;

import java.util.List;
import java.util.UUID;


public interface AdminDashboardStatusService {

    long getTotalAppointmentsToday();

    long getTotalDoctors();

    long getTotalPatients();

    PatientsStats getPatientStats();


    long getPendingDoctorApprovals();

    List<AppointmentFullDto> getRecentAppointments();

    GenderCountResponseDto getPatientsGenderCount();

    List<WeeklyAppointmentCountDto> getWeeklyAppointments();

    long getTotalReportWritten(UUID patientId);

}
