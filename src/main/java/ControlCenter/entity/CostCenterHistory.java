package ControlCenter.entity;

import ControlCenter.entity.compositeKey.CostCenterHistoryId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cost_center_history")
public class CostCenterHistory {
    @EmbeddedId
    private CostCenterHistoryId id;

    @ManyToOne
    @JoinColumn(name = "parent", referencedColumnName = "internal_id", insertable = false, updatable = false)
    private CostCenter costCenter;

    @ManyToOne
    @JoinColumn(name = "parent_unit", referencedColumnName = "internal_id")
    private CostCenter parentUnit;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "record_status", nullable = false)
    private Short recordStatus;

    @Column(name = "end_date")
    private LocalDate endDate;
}
