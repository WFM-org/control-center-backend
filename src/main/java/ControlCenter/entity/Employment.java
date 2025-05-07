package ControlCenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "employment",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tenant", "employee_id"}),
                @UniqueConstraint(columnNames = {"tenant", "username"})
        })
@NoArgsConstructor
@AllArgsConstructor
public class Employment {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false, updatable = false)
    private UUID internalId;

    @Column(name = "tenant", nullable = false)
    private UUID tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person", nullable = false)
    private Person person;

    @Column(name = "employee_id", nullable = false, length = 16)
    private String employeeId;

    @Column(name = "username", length = 128, nullable = false)
    private String username;

    @Column(name = "email", length = 126, nullable = false)
    private String email;

    @Column(name = "password", length = 128,  nullable = false)
    private String password;

    @Column(name = "login_method")
    private Integer loginMethod;

    @Column(name = "primary_employment", nullable = false)
    private Boolean primaryEmployment;

    @ManyToOne
    @JoinColumn(name = "timezone", referencedColumnName = "tz_name")
    private Timezone timezone;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "freeze_access_from")
    private LocalDate freezeAccessFrom;

}