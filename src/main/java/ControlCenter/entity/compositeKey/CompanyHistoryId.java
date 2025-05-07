package ControlCenter.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CompanyHistoryId {
    @Column(name = "parent", nullable = false)
    private UUID parent;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
}
