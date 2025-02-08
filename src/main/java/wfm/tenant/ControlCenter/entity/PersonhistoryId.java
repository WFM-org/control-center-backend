package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class PersonhistoryId implements Serializable {
    private static final long serialVersionUID = -2690768209533544513L;
    @Column(name = "person", nullable = false)
    private UUID person;

    @Column(name = "startdate", nullable = false)
    private LocalDate startdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PersonhistoryId entity = (PersonhistoryId) o;
        return Objects.equals(this.person, entity.person) &&
                Objects.equals(this.startdate, entity.startdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, startdate);
    }

}