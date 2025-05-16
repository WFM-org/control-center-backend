package ControlCenter.entity;

import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.entity.compositeKey.CompanyHistoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "company_history")
public class CompanyHistory {
    @EmbeddedId
    private CompanyHistoryId id;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Name can not be null")
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "default_language_pack", nullable = false)
    private LanguagePack languagePackDefault;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "record_status", length = 64)
    private Short recordStatus;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent", referencedColumnName = "internal_id", insertable = false, updatable = false)
    private Company company;

    public static CompanyHistory fromDTO(CompanyHistoryDTO dto) {
        CompanyHistory companyHistory = new CompanyHistory();
        companyHistory.setId(new CompanyHistoryId(dto.getCompanyId(), dto.getStartDate()));
        companyHistory.setName(dto.getName());
        if(dto.getLanguagePackDefault() != null) {
            companyHistory.setLanguagePackDefault(LanguagePack.fromDTO(dto.getLanguagePackDefault()));
        }
        companyHistory.setTimezone(dto.getTimezone());
        companyHistory.setRecordStatus(dto.getRecordStatus());
        return companyHistory;
    }
}
