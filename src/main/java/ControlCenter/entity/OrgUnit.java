package ControlCenter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orgunit",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tenant", "external_id"})
        })
@Builder
public class OrgUnit {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID internalId;

    @Column(name = "tenant", nullable = false, columnDefinition = "UUID")
    private UUID tenant;

    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;
}