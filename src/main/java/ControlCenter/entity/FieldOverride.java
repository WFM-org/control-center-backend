package ControlCenter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "field_override")
@IdClass(FieldOverrideId.class)
public class FieldOverride {

    @Id
    @Column(name = "tenant", nullable = false)
    private UUID tenantId;

    @Id
    @Column(name = "allowed_field_overrides", nullable = false)
    private Integer allowedFieldOverrideId;

    @NotNull
    @Column(name = "visible", nullable = false)
    private Boolean visible;

    @NotNull
    @Column(name = "mandatory", nullable = false)
    private Boolean mandatory;

    @NotNull
    @Column(name = "editable", nullable = false)
    private Boolean editable;

    // navigation til selve AllowedFieldOverride-entiteten og boojoo:
    @ManyToOne(optional = false)
    @JoinColumn(name = "allowed_field_overrides", insertable = false, updatable = false)
    private AllowedFieldOverrides allowedFieldOverride;

    // navigation til selve tenant-entiteten og daa boojoo:
    @ManyToOne(optional = false)
    @JoinColumn(name = "tenant", insertable = false, updatable = false)
    private Tenant tenant;
}
