package ControlCenter.dto;

import ControlCenter.entity.CompanyHistory;
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
public class CompanyHistoryDTO {
    private UUID internalId;
    private UUID companyId;
    private LocalDate endDate;
    private LocalDate startDate;
    private String name;
    private String country;
    private LanguagePackDTO languagePackDefault;
    private String timezone;
    private Short recordStatus;

    public CompanyHistoryDTO(UUID internalId, UUID companyId, LocalDate startDate, String name, String country, LanguagePackDTO languagePack, String timezone) {
        this.internalId = internalId;
        this.companyId = companyId;
        this.startDate = startDate;
        this.name = name;
        this.country = country;
        this.languagePackDefault = languagePack;
        this.timezone = timezone;
        this.recordStatus = RecordStatus.ACTIVE.getValue();
    }

    public static CompanyHistoryDTO fromEntity(CompanyHistory companyHistory) {
        LanguagePackDTO languagePackDTO = null;
        if(companyHistory.getLanguagePackDefault() != null) {
            languagePackDTO = new LanguagePackDTO(companyHistory.getLanguagePackDefault().getInternalId(),
                    companyHistory.getLanguagePackDefault().getLanguageName());
        }
        UUID companyId = null;
        if(companyHistory.getCompany() != null) {
            companyId = companyHistory.getInternalId();
        }
        return new CompanyHistoryDTO(companyHistory.getInternalId(),
                companyId,
                companyHistory.getEndDate(),
                companyHistory.getStartDate(),
                companyHistory.getName(),
                companyHistory.getCountry(),
                languagePackDTO,
                companyHistory.getTimezone(),
                companyHistory.getRecordStatus());
    }
}
