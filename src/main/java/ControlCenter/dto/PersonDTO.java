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

    public static PersonDTO fromEntity(Person person, LocalDate effectiveDate) {
        List<PersonHistoryDTO> historyList = person.getPersonHistories().stream()
                .map(PersonHistoryDTO::fromEntity).toList();

        var effectiveDated = historyList.stream()
                .filter(h -> h.getStartDate() != null && h.getEndDate() != null)
                .filter(h -> !effectiveDate.isBefore(h.getStartDate()))
                .filter(f -> !effectiveDate.isAfter(f.getEndDate()))
                .findFirst();

        PersonDTO dto = new PersonDTO();
        dto.setInternalId(person.getInternalId());
        dto.setTenant(person.getTenant());
        dto.setPersonId(person.getPersonId());

        dto.setFirstName(effectiveDated.map(PersonHistoryDTO::getFirstName).orElse(null));
        dto.setMiddleName(effectiveDated.map(PersonHistoryDTO::getMiddleName).orElse(null));
        dto.setLastName(effectiveDated.map(PersonHistoryDTO::getLastName).orElse(null));
        dto.setDisplayName(effectiveDated.map(PersonHistoryDTO::getDisplayName).orElse(null));
        dto.setLanguagePack(effectiveDated.map(PersonHistoryDTO::getLanguagePack).orElse(null));
        dto.setStartDate(effectiveDated.map(PersonHistoryDTO::getStartDate).orElse(null));
        dto.setEndDate(effectiveDated.map(PersonHistoryDTO::getEndDate).orElse(null));

        dto.setPersonHistoryList(historyList);

        return dto;
    }
}
