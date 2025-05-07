package ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cost_center",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tenant", "external_id"})
        })
public class CostCenter {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false, updatable = false)
    private UUID internalId;

    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;

    @ManyToOne
    @JoinColumn(name = "tenant", referencedColumnName = "internal_id", insertable = false, updatable = false)
    private Tenant tenant;

}
