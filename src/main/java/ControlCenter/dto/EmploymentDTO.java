package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.Employment;
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

    private List<EmploymentHistoryDTO> employmentHistoryList;

    public static EmploymentDTO fromEntity(Employment entity, LocalDate effectiveDate) {
        List<EmploymentHistoryDTO> histories = entity.getEmploymentHistories().stream()
                .map(EmploymentHistoryDTO::fromEntity)
                .toList();

        Optional<EmploymentHistoryDTO> effective = Optional.empty();
        if (effectiveDate != null) {
            effective = histories.stream()
                    .filter(f -> f.getStartDate() != null && f.getEndDate() != null)
                    .filter(f -> !effectiveDate.isBefore(f.getStartDate()))
                    .filter(f -> effectiveDate.isBefore(f.getEndDate()))
                    .findFirst();
        }

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
                effective.map(EmploymentHistoryDTO::getEvent).orElse(null),
                effective.map(EmploymentHistoryDTO::getEmployeeStatus).orElse(null),
                effective.map(EmploymentHistoryDTO::getCompanyId).orElse(null),
                effective.map(EmploymentHistoryDTO::getTimezone).orElse(null),
                effective.map(EmploymentHistoryDTO::getManagerId).orElse(null),
                effective.map(EmploymentHistoryDTO::getHrId).orElse(null),
                effective.map(EmploymentHistoryDTO::getOrgUnitId).orElse(null),
                effective.map(EmploymentHistoryDTO::getCostCenterId).orElse(null),
                effective.map(EmploymentHistoryDTO::getStartDate).orElse(null),
                effective.map(EmploymentHistoryDTO::getEndDate).orElse(null),
                histories
        );
    }
}
