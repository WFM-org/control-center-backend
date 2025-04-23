package ControlCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LanguagePackEnabledId implements Serializable {

    private static final long serialVersionUID = -1214776407967732525L;

    @Column(name = "language_pack", nullable = false, length = 10)
    private String languagePack;

    @Column(name = "tenant", nullable = false)
    private UUID tenant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LanguagePackEnabledId entity = (LanguagePackEnabledId) o;
        return Objects.equals(this.languagePack, entity.languagePack) &&
                Objects.equals(this.tenant, entity.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languagePack, tenant);
    }
}