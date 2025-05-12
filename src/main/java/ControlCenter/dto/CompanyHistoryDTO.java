package ControlCenter.dto;

import ControlCenter.entity.CompanyHistory;
import ControlCenter.enums.RecordStatus;
import ControlCenter.projection.LanguagePackProjection;
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
public class CompanyHistoryDTO {
    private UUID companyId;
    private LocalDate endDate;
    private LocalDate startDate;
    private String name;
    private LanguagePackProjection languagePackDefault;
    private String timezone;
    private Short recordStatus;

    public CompanyHistoryDTO(UUID companyId, LocalDate startDate, String name, LanguagePackProjection languagePack, String timezone) {
        this.companyId = companyId;
        this.startDate = startDate;
        this.name = name;
        this.languagePackDefault = languagePack;
        this.timezone = timezone;
        this.recordStatus = RecordStatus.ACTIVE.getValue();
    }

    public static CompanyHistoryDTO fromEntity(CompanyHistory companyHistory) {
        return new CompanyHistoryDTO(companyHistory.getId().getParent(),
                companyHistory.getEndDate(),
                companyHistory.getId().getStartDate(),
                companyHistory.getName(),
                LanguagePackProjection.mapToLanguagePackProjection(companyHistory.getLanguagePackDefault()),
                companyHistory.getTimezone(),
                companyHistory.getRecordStatus());
    }
}
