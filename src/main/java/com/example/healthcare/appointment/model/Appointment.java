package com.example.healthcare.appointment.model;

import com.example.healthcare.appointment.enums.AppointmentStatus;
import com.example.healthcare.appointmentRequest.model.AppointmentRequest;
import com.example.healthcare.common.enums.DoctorCheckType;
import com.example.healthcare.common.model.BaseEntity;
import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.model.DoctorSchedule;
import com.example.healthcare.patient.model.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"doctor", "patient", "schedule", "appointmentRequest"})
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private DoctorSchedule schedule;

    private LocalDateTime appointmentDate;
    private String meetingLink;
    private String meetingToken;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private DoctorCheckType checkupType;

    @OneToOne
    @JoinColumn(name = "appointment_request_id", unique = true)
    private AppointmentRequest appointmentRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}