package com.example.healthcare.doctor.model;

import com.example.healthcare.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "doctorProfile")
@Table(
        name = "doctor_schedules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_doctor_schedule_slot",
                        columnNames = {
                                "doctor_id",
                                "schedule_date",
                                "start_time"
                        }
                )
        }
)
public class DoctorSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate scheduleDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean available;

    @Builder.Default
    private boolean isLocked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctorProfile;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoctorSchedule)) return false;
        DoctorSchedule that = (DoctorSchedule) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}