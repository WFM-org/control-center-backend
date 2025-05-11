package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.Company;
import ControlCenter.entity.LanguagePack;
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
    private LanguagePack languagePackDefault;
    @Historical
    private String timezone;
    private List<CompanyHistoryDTO> historicalData;

    public static CompanyDTO fromEntity(Company company, LocalDate effectiveDate) {
        List<CompanyHistoryDTO> historicalData = company.getCompanyHistories().stream()
                .map(CompanyHistoryDTO::fromEntity).toList();

        Optional<CompanyHistoryDTO> effectiveDated = Optional.empty();
        if(effectiveDate != null) {
            effectiveDated = historicalData.stream()
                    .filter(f ->
                            (effectiveDate.isEqual(f.getStartDate()) || effectiveDate.isAfter(f.getStartDate()))
                                    && effectiveDate.isBefore(f.getEndDate()))
                    .findFirst();
        }

        return new CompanyDTO(company.getInternalId(),
                company.getTenant(),
                company.getExternalId(),
                effectiveDated.map(CompanyHistoryDTO::getName).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getLanguagePackDefault).orElse(null),
                effectiveDated.map(CompanyHistoryDTO::getTimezone).orElse(null),
                historicalData);
    }
}
