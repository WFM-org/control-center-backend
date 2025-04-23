package ControlCenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "orgunit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgUnit {

    @Id
    @GeneratedValue
    @Column(name = "internal_id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID internalId;

    @Column(name = "tenant", nullable = false, columnDefinition = "UUID")
    private UUID tenant;

    @Column(name = "external_id", nullable = false, length = 16)
    private String externalId;
}
