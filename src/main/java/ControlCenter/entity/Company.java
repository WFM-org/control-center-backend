package ControlCenter.entity;

import ControlCenter.dto.CompanyDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ControlCenter.annotations.ImmutableField;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false)
    private UUID internalId;

    @Column(name = "tenant", nullable = false)
    @ImmutableField
    private UUID tenant;

    @NotNull(message = "External Id can not be null")
    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<CompanyHistory> companyHistories = new ArrayList<>();

    public static Company fromDTO(CompanyDTO dto) {
        Company company = new Company();
        company.setExternalId(dto.getExternalId());
        company.setTenant(dto.getTenant());
        return new Company();
    }
}
