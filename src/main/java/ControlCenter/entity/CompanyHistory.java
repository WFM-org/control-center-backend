package ControlCenter.entity;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "default_language_pack", nullable = false)
    private LanguagePack languagePackDefault;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "record_status", length = 64)
    private Short recordStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent", referencedColumnName = "internal_id", insertable = false, updatable = false)
    private Company company;
}
