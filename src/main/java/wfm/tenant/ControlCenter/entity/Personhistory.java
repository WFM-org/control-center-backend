package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "personhistory")
public class Personhistory {
    @EmbeddedId
    private PersonhistoryId id;

    @MapsId("person")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person", nullable = false)
    private Person person;

    @Column(name = "enddate")
    private LocalDate enddate;

    @Column(name = "firstname", length = 64)
    private String firstname;

    @Column(name = "middlename", length = 64)
    private String middlename;

    @Column(name = "lastname", length = 64)
    private String lastname;

    @Column(name = "displayname", length = 128)
    private String displayname;

}