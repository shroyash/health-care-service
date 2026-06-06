package com.example.healthcare.report.model;

import com.example.healthcare.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "medicalReport")
@Table(name = "report_medicines")
public class ReportMedicine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private MedicalReport medicalReport;

    private String name;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;

    // ✅ equals/hashCode based on id only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportMedicine)) return false;
        ReportMedicine that = (ReportMedicine) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}