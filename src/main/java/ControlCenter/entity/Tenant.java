package ControlCenter.entity;

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
    @Column(name = "internal_id", nullable = false)
    private UUID Id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    @Column(name = "tenant_id", nullable = false, length = 16)
    private String tenantId;

    @Column(name = "record_status", nullable = false, length = 64)
    private Short recordStatus;

    @Column(name = "tenant_name", nullable = false, length = 64)
    private String tenantName;

    @Column(name = "tenant_type", nullable = false, length = 64)
    private String tenantType;

    @Column(name = "admin_email", nullable = false, length = 128)
    private String adminEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "communication_language", nullable = false)
    private LanguagePack languagePackDefault;

}