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
public class EmploymenthistoryId implements Serializable {
    private static final long serialVersionUID = 4249780623728591485L;
    @Column(name = "employment", nullable = false)
    private UUID employment;

    @Column(name = "start_date", nullable = false)
    private LocalDate startdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EmploymenthistoryId entity = (EmploymenthistoryId) o;
        return Objects.equals(this.employment, entity.employment) &&
                Objects.equals(this.startdate, entity.startdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employment, startdate);
    }

}