package wfm.tenant.ControlCenter.entity;

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
public class LocaleenabledId implements Serializable {

    private static final long serialVersionUID = -1214776407967732525L;

    @Column(name = "locale", nullable = false, length = 10)
    private String locale;

    @Column(name = "tenant", nullable = false)
    private UUID tenant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LocaleenabledId entity = (LocaleenabledId) o;
        return Objects.equals(this.locale, entity.locale) &&
                Objects.equals(this.tenant, entity.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, tenant);
    }
}