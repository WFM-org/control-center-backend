package ControlCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgUnitHistoryId implements Serializable {

    @Column(name = "orgunit")
    private UUID orgUnit;

    @Column(name = "start_date")
    private LocalDate startDate;
}
