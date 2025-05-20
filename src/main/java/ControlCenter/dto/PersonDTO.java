package ControlCenter.dto;

import ControlCenter.annotations.Historical;
import ControlCenter.entity.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private UUID internalId;
    private UUID tenant;
    private String personId;
    @Historical
    private String firstName;
    @Historical
    private String middleName;
    @Historical
    private String lastName;
    @Historical
    private String displayName;
    @Historical
    private LanguagePackDTO languagePack;
    @Historical
    private LocalDate startDate;
    @Historical
    private LocalDate endDate;
    @JsonProperty("historical")
    private List<PersonHistoryDTO> personHistoryList;

    public static PersonDTO fromEntity(Person person, Optional<PersonHistoryDTO> effectiveDated) {
        List<PersonHistoryDTO> historyList = person.getPersonHistories().stream()
                .map(PersonHistoryDTO::fromEntity).toList();
        return new PersonDTO(
                person.getInternalId(),
                person.getTenant(),
                person.getPersonId(),
                effectiveDated.map(PersonHistoryDTO::getFirstName).orElse(null),
                effectiveDated.map(PersonHistoryDTO::getMiddleName).orElse(null),
                effectiveDated.map(PersonHistoryDTO::getLastName).orElse(null),
                effectiveDated.map(PersonHistoryDTO::getDisplayName).orElse(null),
                effectiveDated.map(PersonHistoryDTO::getLanguagePack).orElse(null),
                effectiveDated.map(PersonHistoryDTO::getStartDate).orElse(null),
                effectiveDated.map(PersonHistoryDTO::getEndDate).orElse(null),
                historyList);
    }
}
