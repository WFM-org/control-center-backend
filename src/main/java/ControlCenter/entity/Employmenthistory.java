package ControlCenter.entity;

import ControlCenter.entity.compositeKey.EmploymenthistoryId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employment_history")
public class Employmenthistory {
    @EmbeddedId
    private EmploymenthistoryId id;

    @MapsId("parent")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private Employment parent;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "event", nullable = false)
    private short event;

    @Column(name = "employee_status", nullable = false)
    private short employeeStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Employment manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr")
    private Employment hr;

    @ManyToOne
    @JoinColumn(name = "orgunit", referencedColumnName = "internal_id")
    private OrgUnit orgunit;

    @ManyToOne
    @JoinColumn(name = "cost_center", referencedColumnName = "internal_id")
    private CostCenter costCenter;
}