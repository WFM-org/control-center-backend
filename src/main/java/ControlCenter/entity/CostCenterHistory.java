package ControlCenter.entity;

import ControlCenter.annotations.ImmutableField;
import ControlCenter.dto.CostCenterHistoryDTO;
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
@Table(name = "cost_center_history")
public class CostCenterHistory {

    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private CostCenter costCenter;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Name can not be null")
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "record_status", length = 64)
    private Short recordStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_unit")
    private CostCenter parentUnit;

    public static CostCenterHistory fromDTO(CostCenterHistoryDTO dto, CostCenter cc) {
        CostCenterHistory cch = new CostCenterHistory();
        cch.setCostCenter(cc);
        cch.setInternalId(dto.getInternalId());
        cch.setStartDate(dto.getStartDate());
        cch.setEndDate(dto.getEndDate());
        cch.setName(dto.getName());
        cch.setRecordStatus(dto.getRecordStatus());
        return cch;
    }
}
