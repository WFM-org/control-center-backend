package ControlCenter.dto;

import ControlCenter.entity.CostCenterHistory;
import ControlCenter.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostCenterHistoryDTO {
    private UUID internalId;
    private UUID costCenterId;
    private LocalDate endDate;
    private LocalDate startDate;
    private String name;
    private Short recordStatus;
    private UUID parentUnitId;

    public CostCenterHistoryDTO(UUID internalId, UUID costCenterId, LocalDate startDate, String name, UUID parentUnitId) {
        this.internalId = internalId;
        this.costCenterId = costCenterId;
        this.startDate = startDate;
        this.name = name;
        this.parentUnitId = parentUnitId;
        this.recordStatus = RecordStatus.ACTIVE.getValue();
    }

    public static CostCenterHistoryDTO fromEntity(CostCenterHistory cch) {
        UUID parentUnitId = null;
        if (cch.getParentUnit() != null) {
            parentUnitId = cch.getParentUnit().getInternalId();
        }

        return new CostCenterHistoryDTO(
                cch.getInternalId(),
                cch.getCostCenter().getInternalId(),
                cch.getEndDate(),
                cch.getStartDate(),
                cch.getName(),
                cch.getRecordStatus(),
                parentUnitId
        );
    }
}
