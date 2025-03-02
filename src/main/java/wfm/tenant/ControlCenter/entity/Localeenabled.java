package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "localeenabled")
public class Localeenabled {
    @EmbeddedId
    private LocaleenabledId id;

    @MapsId("locale")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "locale", nullable = false)
    private Locale locale;

    @MapsId("tenant")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant", nullable = false)
    private Tenant tenant;
}