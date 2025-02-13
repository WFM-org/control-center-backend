package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internalid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant", nullable = false)
    private Tenant tenant;

    @Column(name = "externalid", nullable = false, length = 16)
    private String externalId;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localedefault")
    private Locale localedefault;

}