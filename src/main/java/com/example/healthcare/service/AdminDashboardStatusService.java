package com.example.healthcare.service;

import com.example.healthcare.dto.AppointmentFullDto;
import com.example.healthcare.dto.PatientsStats;

import java.util.List;


public interface AdminDashboardStatusService {

    long getTotalAppointmentsToday();

    long getTotalDoctors();

    long getTotalPatients();

    PatientsStats getPatientStats();


    long getPendingDoctorApprovals();

    List<AppointmentFullDto> getRecentAppointments();


}
