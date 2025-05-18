package ControlCenter.entity;

import ControlCenter.dto.OrgUnitHistoryDTO;
import ControlCenter.annotations.ImmutableField;
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
@Table(name = "orgunit_history")
public class OrgUnitHistory {
    @Id
    @GeneratedValue
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private OrgUnit orgUnit;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "record_status", nullable = false)
    private Short recordStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_unit")
    private OrgUnit parentUnit;

    public static OrgUnitHistory fromDTO(OrgUnitHistoryDTO dto, OrgUnit orgUnit) {
        OrgUnitHistory history = new OrgUnitHistory();
        history.setOrgUnit(orgUnit);
        history.setInternalId(dto.getInternalId());
        history.setStartDate(dto.getStartDate());
        history.setEndDate(dto.getEndDate());
        history.setName(dto.getName());
        history.setRecordStatus(dto.getRecordStatus());
        if (dto.getParentUnitId() != null) {
            OrgUnit parent = new OrgUnit();
            parent.setInternalId(dto.getParentUnitId());
            history.setParentUnit(parent);
        }
        return history;
    }
}
