package ControlCenter.entity;

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

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "primary_employment", nullable = false)
    private Boolean primaryemployment = false;

    @Column(name = "freeze_access_from")
    private LocalDate freezeAccessFrom;

}