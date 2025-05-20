package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.OrgUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrgUnitDTO {
    private UUID internalId;
    private UUID tenant;
    private String externalId;
    @Historical
    private String name;
    @Historical
    private UUID parentUnitId;
    @Historical
    private LocalDate startDate;
    @Historical
    private LocalDate endDate;
    @Historical
    private Short recordStatus;
    @JsonProperty("historical")
    private List<OrgUnitHistoryDTO> orgUnitHistoryList;

    public static OrgUnitDTO fromEntity(OrgUnit orgUnit, LocalDate effectiveDate) {
        List<OrgUnitHistoryDTO> historyList = orgUnit.getOrgUnitHistories().stream()
                .map(OrgUnitHistoryDTO::fromEntity)
                .toList();

        Optional<OrgUnitHistoryDTO> effectiveDated = Optional.empty();
        if (effectiveDate != null) {
            effectiveDated = historyList.stream()
                    .filter(f -> f.getStartDate() != null && f.getEndDate() != null)
                    .filter(f -> !effectiveDate.isBefore(f.getStartDate()))
                    .filter(f -> !effectiveDate.isAfter(f.getEndDate()))
                    .findFirst();
        }

        return new OrgUnitDTO(
                orgUnit.getInternalId(),
                orgUnit.getTenant(),
                orgUnit.getExternalId(),
                effectiveDated.map(OrgUnitHistoryDTO::getName).orElse(null),
                effectiveDated.map(OrgUnitHistoryDTO::getParentUnitId).orElse(null),
                effectiveDated.map(OrgUnitHistoryDTO::getStartDate).orElse(null),
                effectiveDated.map(OrgUnitHistoryDTO::getEndDate).orElse(null),
                effectiveDated.map(OrgUnitHistoryDTO::getRecordStatus).orElse(null),
                historyList
        );
    }
}
