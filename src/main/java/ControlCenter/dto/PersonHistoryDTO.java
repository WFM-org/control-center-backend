package ControlCenter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PersonHistoryDTO {
    private UUID internalId;
    private UUID personId;
    private LocalDate startDate;

    public PersonHistoryDTO(UUID internalId, UUID personId, LocalDate startDate, LocalDate endDate, String firstName, String middleName, String lastName, String displayName, LanguagePackDTO languagePack) {
        this.internalId = internalId;
        this.personId = personId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.languagePack = languagePack;
    }

    private LocalDate endDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;
    private LanguagePackDTO languagePack;


    public static PersonHistoryDTO fromEntity(ControlCenter.entity.PersonHistory history) {
        LanguagePackDTO langPackDTO = null;
        if (history.getPerson() != null && history.getPerson().getLanguagePack() != null) {
            langPackDTO = new LanguagePackDTO(history.getPerson().getLanguagePack().getInternalId(),
                    history.getPerson().getLanguagePack().getLanguageName());
        }

        UUID personId = null;
        if (history.getPerson() != null) {
            personId = history.getPerson().getInternalId();
        }

        return new PersonHistoryDTO(
                history.getInternalId(),
                personId,
                history.getStartDate(),
                history.getEndDate(),
                history.getFirstName(),
                history.getMiddleName(),
                history.getLastName(),
                history.getDisplayName(),
                langPackDTO);
    }
}
