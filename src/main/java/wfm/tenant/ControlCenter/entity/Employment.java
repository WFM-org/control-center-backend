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
    @Column(name = "internalid", nullable = false)
    private UUID id;

    @Column(name = "tenant", nullable = false)
    private UUID tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person", nullable = false)
    private Person person;

    @Column(name = "employeeid", nullable = false, length = 16)
    private String employeeid;

    @Column(name = "primaryemployment", nullable = false)
    private Boolean primaryemployment = false;

    @Column(name = "hiredate", nullable = false)
    private LocalDate hiredate;
    @Column(name = "terminationdate")
    private LocalDate terminationdate;

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "status not null")
    private Object status;
*/
}