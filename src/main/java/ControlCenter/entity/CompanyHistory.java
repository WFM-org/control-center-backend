package ControlCenter.entity;

import ControlCenter.annotations.ImmutableField;
import ControlCenter.dto.CompanyHistoryDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company_history")
public class CompanyHistory {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private Company company;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Name can not be null")
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @NotNull(message = "Country can not be null")
    @Column(name = "country", nullable = false, length = 64)
    private String country;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "default_language_pack", nullable = true)
    private LanguagePack languagePackDefault;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "record_status", length = 64)
    private Short recordStatus;

    public static CompanyHistory fromDTO(CompanyHistoryDTO dto, Company company) {
        CompanyHistory companyHistory = new CompanyHistory();
        companyHistory.setCompany(company);
        companyHistory.setInternalId(dto.getInternalId());
        companyHistory.setStartDate(dto.getStartDate());
        companyHistory.setName(dto.getName());
        companyHistory.setCountry(dto.getCountry());
        if(dto.getLanguagePackDefault() != null) {
            companyHistory.setLanguagePackDefault(LanguagePack.fromDTO(dto.getLanguagePackDefault()));
        }
        companyHistory.setTimezone(dto.getTimezone());
        companyHistory.setRecordStatus(dto.getRecordStatus());
        return companyHistory;
    }
}
