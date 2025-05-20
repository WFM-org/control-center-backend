package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.CostCenter;
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
public class CostCenterDTO {
    private UUID internalId;
    private UUID tenant;
    private String externalId;
    @Historical
    private String name;
    @Historical
    private LocalDate startDate;
    @Historical
    private Short recordStatus;
    private UUID parentUnitId;
    @JsonProperty("historical")
    private List<CostCenterHistoryDTO> costCenterHistoryList;

    public static CostCenterDTO fromEntity(CostCenter cc, LocalDate effectiveDate) {
        List<CostCenterHistoryDTO> historyList = cc.getCostCenterHistories().stream()
                .map(CostCenterHistoryDTO::fromEntity).toList();

        Optional<CostCenterHistoryDTO> effectiveDated = Optional.empty();
        if (effectiveDate != null) {
            effectiveDated = historyList.stream()
                    .filter(f -> f.getStartDate() != null && f.getEndDate() != null)
                    .filter(f -> !effectiveDate.isBefore(f.getStartDate()))
                    .filter(f -> !effectiveDate.isAfter(f.getEndDate()))
                    .findFirst();
        }

        return new CostCenterDTO(
                cc.getInternalId(),
                cc.getTenant(),
                cc.getExternalId(),
                effectiveDated.map(CostCenterHistoryDTO::getName).orElse(null),
                effectiveDated.map(CostCenterHistoryDTO::getStartDate).orElse(null),
                effectiveDated.map(CostCenterHistoryDTO::getRecordStatus).orElse(null),
                effectiveDated.map(CostCenterHistoryDTO::getParentUnitId).orElse(null),
                historyList
        );
    }
}
