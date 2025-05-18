package ControlCenter.entity;

import ControlCenter.dto.CostCenterDTO;
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
@Table(name = "cost_center")
public class CostCenter {

    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @Column(name = "tenant", nullable = false)
    @ImmutableField
    private UUID tenant;

    @NotNull(message = "External Id can not be null")
    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;

    @OneToMany(mappedBy = "costCenter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CostCenterHistory> costCenterHistories;

    public void addCostCenterHistory(CostCenterHistory history) {
        if (costCenterHistories == null) {
            costCenterHistories = new ArrayList<>();
        }
        costCenterHistories.add(history);
        history.setCostCenter(this);
    }

    public static CostCenter fromDTO(CostCenterDTO dto) {
        CostCenter cc = new CostCenter();
        cc.setExternalId(dto.getExternalId());
        cc.setTenant(dto.getTenant());
        return cc;
    }
}
