package ControlCenter.dto;

import ControlCenter.entity.CompanyHistory;
import ControlCenter.entity.LanguagePack;
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
    private LanguagePack languagePackDefault;
    private String timezone;
    private Short recordStatus;

    public CompanyHistoryDTO(UUID companyId, LocalDate startDate, String name, LanguagePack languagePack, String timezone) {
        this.companyId = companyId;
        this.startDate = startDate;
        this.name = name;
        this.languagePackDefault = languagePack;
        this.timezone = timezone;
    }

    public static CompanyHistoryDTO fromEntity(CompanyHistory companyHistory) {
        return new CompanyHistoryDTO(companyHistory.getCompany().getInternalId(),
                companyHistory.getEndDate(),
                companyHistory.getId().getStartDate(),
                companyHistory.getName(),
                companyHistory.getLanguagePackDefault(),
                companyHistory.getTimezone(),
                companyHistory.getRecordStatus());
    }
}
