package ControlCenter.dto;

import ControlCenter.entity.EmploymentHistory;
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
public class EmploymentHistoryDTO {
    private UUID internalId;
    private UUID parentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Short event;
    private Short employeeStatus;
    private UUID companyId;
    private String timezone;
    private UUID managerId;
    private UUID hrId;
    private UUID orgUnitId;
    private UUID costCenterId;

    public static EmploymentHistoryDTO fromEntity(EmploymentHistory entity) {
        return new EmploymentHistoryDTO(
                entity.getInternalId(),
                entity.getEmployment() != null ? entity.getEmployment().getInternalId() : null,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getEvent(),
                entity.getEmployeeStatus(),
                entity.getCompany() != null ? entity.getCompany().getInternalId() : null,
                entity.getTimezone(),
                entity.getManager() != null ? entity.getManager().getInternalId() : null,
                entity.getHr() != null ? entity.getHr().getInternalId() : null,
                entity.getOrgUnit() != null ? entity.getOrgUnit().getInternalId() : null,
                entity.getCostCenter() != null ? entity.getCostCenter().getInternalId() : null
        );
    }
}
