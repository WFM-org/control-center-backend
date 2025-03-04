package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language_pack_enabled")
public class LanguagePackEnabled {
    @EmbeddedId
    private LanguagePackEnabledId id;

    @MapsId("language_pack")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_pack", referencedColumnName = "internal_id", nullable = false)
    private LanguagePack languagePack;

    @MapsId("tenant")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant", nullable = false)
    private Tenant tenant;
}