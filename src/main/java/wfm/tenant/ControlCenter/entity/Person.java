package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "internalid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant", nullable = false)
    private Tenant tenant;

    @Column(name = "personid", nullable = false, length = 16)
    private String personid;

    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "email", length = 126)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "localedecision")
    private Locale localedecision;

}