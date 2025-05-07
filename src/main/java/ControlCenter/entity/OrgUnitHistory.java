package ControlCenter.entity;

import ControlCenter.entity.compositeKey.OrgUnitHistoryId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orgunit_history")
@NoArgsConstructor
@AllArgsConstructor
public class OrgUnitHistory {
    @EmbeddedId
    private OrgUnitHistoryId id;

    @MapsId("parent")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", nullable = false)
    private OrgUnit parent;

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