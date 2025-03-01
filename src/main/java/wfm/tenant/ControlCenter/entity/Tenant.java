package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tenant")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "internalid", nullable = false)
    private UUID Id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    @Column(name = "tenantid", nullable = false, length = 16)
    private String tenantId;

    @Column(name = "recordstatus", nullable = false, length = 64)
    private Short recordStatus;

    @Column(name = "tenantname", nullable = false, length = 64)
    private String tenantName;

    @Column(name = "tenanttype", nullable = false, length = 64)
    private String tenantType;

    @Column(name = "adminemail", nullable = false, length = 128)
    private String adminEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "localedefault", nullable = false)
    private Locale localeDefault;

}