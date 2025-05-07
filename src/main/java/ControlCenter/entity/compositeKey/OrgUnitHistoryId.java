package ControlCenter.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrgUnitHistoryId implements Serializable {

    @Column(name = "parent")
    private UUID parent;

    @Column(name = "start_date")
    private LocalDate startDate;
}