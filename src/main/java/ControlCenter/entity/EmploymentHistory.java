package ControlCenter.entity;

import ControlCenter.annotations.ImmutableField;
import ControlCenter.dto.EmploymentHistoryDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "employment_history")
public class EmploymentHistory {

    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private Employment employment;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "event", nullable = false)
    private Short event;

    @Column(name = "employee_status", nullable = false)
    private Short employeeStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company", nullable = false)
    private Company company;

    @Column(name = "timezone")
    private String timezone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager")
    private Employment manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hr")
    private Employment hr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orgunit")
    private OrgUnit orgUnit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cost_center")
    private CostCenter costCenter;

    public static EmploymentHistory fromDTO(EmploymentHistoryDTO dto, Employment parent) {
        EmploymentHistory hist = new EmploymentHistory();
        hist.setInternalId(dto.getInternalId());
        hist.setEmployment(parent);
        hist.setStartDate(dto.getStartDate());
        hist.setEndDate(dto.getEndDate());
        hist.setEvent(dto.getEvent());
        hist.setEmployeeStatus(dto.getEmployeeStatus());
        hist.setTimezone(dto.getTimezone());
        // Relations (company, manager, etc.) should be resolved externally (e.g. service layer)
        return hist;
    }
}
