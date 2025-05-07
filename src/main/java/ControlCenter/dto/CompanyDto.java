package ControlCenter.dto;

import ControlCenter.annotations.ImmutableField;
import ControlCenter.entity.LanguagePack;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

public class CompanyDto {

    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false)
    private UUID internalId;

    private UUID effectiveDatedInternalId;

    @Column(name = "tenant", nullable = false)
    @ImmutableField
    private UUID tenant;

    @NotNull(message = "External Id can not be null")
    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Name can not be null")
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "default_language_pack", nullable = false)
    private LanguagePack languagePackDefault;

    @Column(name = "record_status", length = 64)
    private Short recordStatus;
}
