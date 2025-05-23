package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.Employment;
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
public class EmploymentDTO {
    private UUID internalId;
    private UUID tenant;
    private UUID person;
    private String employeeId;
    private String username;
    private String email;
    private String password;
    private Integer loginMethod;
    private Boolean primaryEmployment;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private LocalDate freezeAccessFrom;
    @Historical
    private Short event;
    @Historical
    private Short employeeStatus;
    @Historical
    private UUID companyId;
    @Historical
    private String timezone;
    @Historical
    private UUID managerId;
    @Historical
    private UUID hrId;
    @Historical
    private UUID orgUnitId;
    @Historical
    private UUID costCenterId;
    @Historical
    private LocalDate startDate;
    @Historical
    private LocalDate endDate;
    @JsonProperty("historical")
    private List<EmploymentHistoryDTO> employmentHistoryList;

    public static EmploymentDTO fromEntity(Employment entity, Optional<EmploymentHistoryDTO> effectiveDated) {
        List<EmploymentHistoryDTO> histories = entity.getEmploymentHistories().stream()
                .map(EmploymentHistoryDTO::fromEntity)
                .toList();
        return new EmploymentDTO(
                entity.getInternalId(),
                entity.getTenant(),
                entity.getPerson(),
                entity.getEmployeeId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getLoginMethod(),
                entity.getPrimaryEmployment(),
                entity.getHireDate(),
                entity.getTerminationDate(),
                entity.getFreezeAccessFrom(),
                effectiveDated.map(EmploymentHistoryDTO::getEvent).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getEmployeeStatus).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getCompanyId).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getTimezone).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getManagerId).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getHrId).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getOrgUnitId).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getCostCenterId).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getStartDate).orElse(null),
                effectiveDated.map(EmploymentHistoryDTO::getEndDate).orElse(null),
                histories
        );
    }
}
