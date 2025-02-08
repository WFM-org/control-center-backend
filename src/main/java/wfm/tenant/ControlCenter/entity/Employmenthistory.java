package wfm.tenant.ControlCenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employmenthistory")
public class Employmenthistory {
    @EmbeddedId
    private EmploymenthistoryId id;

    @MapsId("employment")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employment", nullable = false)
    private Employment employment;

    @Column(name = "enddate")
    private LocalDate enddate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Employment manager;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr")
    private Employment hr;

/*
 TODO [Reverse Engineering] create field to map the 'event' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "event", columnDefinition = "eventtype not null")
    private Object event;
*/
/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "status not null")
    private Object status;
*/
}