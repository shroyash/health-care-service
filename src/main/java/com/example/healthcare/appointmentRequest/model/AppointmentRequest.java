package com.example.healthcare.appointmentRequest.model;


import com.example.healthcare.appointmentRequest.enums.AppointmentRequestStatus;
import com.example.healthcare.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(
        name = "appointment_requests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_patient_doctor_date_time",
                        columnNames = {"patient_id", "doctor_id", "date", "start_time"}
                )
        }
)
public class AppointmentRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID patientId;
    private String patientFullName;
    private UUID doctorId;
    private String doctorFullName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentRequestStatus status;

    private String notes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentRequest)) return false;
        AppointmentRequest that = (AppointmentRequest) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}