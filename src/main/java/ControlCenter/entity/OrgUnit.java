package ControlCenter.entity;

import ControlCenter.dto.OrgUnitDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import ControlCenter.annotations.ImmutableField;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orgunit")
public class OrgUnit {
    @Id
    @GeneratedValue
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @Column(name = "tenant", nullable = false)
    @ImmutableField
    private UUID tenant;

    @NotNull
    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;

    @OneToMany(mappedBy = "orgUnit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrgUnitHistory> orgUnitHistories;

    public void addOrgUnitHistory(OrgUnitHistory history) {
        if (orgUnitHistories == null) {
            orgUnitHistories = new ArrayList<>();
        }
        orgUnitHistories.add(history);
        history.setOrgUnit(this);
    }

    public void removeOrgUnitHistory(OrgUnitHistory history) {
        if (orgUnitHistories != null) {
            orgUnitHistories.remove(history);
        }
    }

    public static OrgUnit fromDTO(OrgUnitDTO dto) {
        OrgUnit unit = new OrgUnit();
        unit.setExternalId(dto.getExternalId());
        unit.setTenant(dto.getTenant());
        return unit;
    }
}
