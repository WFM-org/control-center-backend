package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class CountrytocompanyId implements Serializable {
    private static final long serialVersionUID = 5117148570508155459L;
    @Column(name = "country", nullable = false, length = 3)
    private String country;

    @Column(name = "company", nullable = false)
    private UUID company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CountrytocompanyId entity = (CountrytocompanyId) o;
        return Objects.equals(this.country, entity.country) &&
                Objects.equals(this.company, entity.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, company);
    }

}