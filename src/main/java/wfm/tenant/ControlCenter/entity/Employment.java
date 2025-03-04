package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "employment")
public class Employment {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internal_id", nullable = false)
    private UUID id;

    @Column(name = "tenant", nullable = false)
    private UUID tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person", nullable = false)
    private Person person;

    @Column(name = "employee_id", nullable = false, length = 16)
    private String employeeid;

    @Column(name = "username", length = 128, nullable = false)
    private String username;

    @Column(name = "email", length = 126, nullable = false)
    private String email;

    @Column(name = "password", length = 128,  nullable = false)
    private String password;

    @Column(name = "employee_status", nullable = false, length = 64)
    private Short employeeStatus;

    @Column(name = "primary_employment", nullable = false)
    private Boolean primaryemployment = false;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hiredate;

    @Column(name = "termination_date")
    private LocalDate terminationdate;

    @Column(name = "freeze_access_from")
    private LocalDate freezeAccessFrom;

}