package com.example.healthcare.service;

import com.example.healthcare.dto.response.AppointmentFullDto;
import com.example.healthcare.dto.response.GenderCountResponseDto;
import com.example.healthcare.dto.response.PatientsStats;
import com.example.healthcare.dto.response.WeeklyAppointmentCountDto;

import java.util.List;


public interface AdminDashboardStatusService {

    long getTotalAppointmentsToday();

    long getTotalDoctors();

    long getTotalPatients();

    PatientsStats getPatientStats();


    long getPendingDoctorApprovals();

    List<AppointmentFullDto> getRecentAppointments();

    GenderCountResponseDto getPatientsGenderCount();

    List<WeeklyAppointmentCountDto> getWeeklyAppointments();


}
