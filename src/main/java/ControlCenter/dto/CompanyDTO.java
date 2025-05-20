package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.Company;
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
public class CompanyDTO {
    private UUID internalId;
    private UUID tenant;
    private String externalId;
    @Historical
    private String name;
    @Historical
    private String country;
    @Historical
    private LanguagePackDTO languagePackDefault;
    @Historical
    private String timezone;
    @Historical
    private LocalDate startDate;
    @Historical
    private Short recordStatus;
    @JsonProperty("historical")
    private List<CompanyHistoryDTO> companyHistoryList;

    public static CompanyDTO fromEntity(Company company, Optional<CompanyHistoryDTO> effectiveDated) {
        List<CompanyHistoryDTO> historyList = company.getCompanyHistories().stream()
                .map(CompanyHistoryDTO::fromEntity).toList();
        return new CompanyDTO(company.getInternalId(),
                company.getTenant(),
                company.getExternalId(),
                effectiveDated.map(CompanyHistoryDTO::getName).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getCountry).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getLanguagePackDefault).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getTimezone).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getStartDate).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getRecordStatus).orElse(null),
                historyList);
    }
}
