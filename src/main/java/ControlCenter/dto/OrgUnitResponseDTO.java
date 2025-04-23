package ControlCenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgUnitResponseDTO {
    private String externalId;
    private String name;
    private Short recordStatus;
    private LocalDate startDate;
    private LocalDate endDate;
}
