package ControlCenter.dto;

import ControlCenter.entity.OrgUnitHistory;
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
public class OrgUnitHistoryDTO {
    private UUID internalId;
    private UUID orgUnitId;
    private LocalDate endDate;
    private LocalDate startDate;
    private String name;
    private Short recordStatus;
    private UUID parentUnitId;

    public OrgUnitHistoryDTO(UUID internalId, UUID orgUnitId, LocalDate startDate, String name, UUID parentUnitId) {
        this.internalId = internalId;
        this.orgUnitId = orgUnitId;
        this.startDate = startDate;
        this.name = name;
        this.parentUnitId = parentUnitId;
        this.recordStatus = RecordStatus.ACTIVE.getValue();
    }

    public static OrgUnitHistoryDTO fromEntity(OrgUnitHistory history) {
        UUID parentUnitId = history.getParentUnit() != null ? history.getParentUnit().getInternalId() : null;
        return new OrgUnitHistoryDTO(
                history.getInternalId(),
                history.getOrgUnit().getInternalId(),
                history.getEndDate(),
                history.getStartDate(),
                history.getName(),
                history.getRecordStatus(),
                parentUnitId
        );
    }
}
