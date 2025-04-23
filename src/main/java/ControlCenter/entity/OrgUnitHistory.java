package ControlCenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "orgunit_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgUnitHistory {

    @EmbeddedId
    private OrgUnitHistoryId id;

    @MapsId("orgUnit")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgunit", nullable = false)
    private OrgUnit orgUnit;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "record_status", nullable = false)
    private Short recordStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_unit")
    private OrgUnit parentUnit;
}


