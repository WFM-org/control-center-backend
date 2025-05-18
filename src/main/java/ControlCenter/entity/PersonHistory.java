package ControlCenter.entity;

import ControlCenter.dto.PersonHistoryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ControlCenter.annotations.ImmutableField;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "person_history")
public class PersonHistory {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent", nullable = false)
    private Person person;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "first_name", length = 64)
    private String firstName;

    @Column(name = "middle_name", length = 64)
    private String middleName;

    @Column(name = "last_name", length = 64)
    private String lastName;

    @Column(name = "display_name", length = 128)
    private String displayName;

    public static PersonHistory fromDTO(PersonHistoryDTO dto, Person person) {
        PersonHistory history = new PersonHistory();
        history.setPerson(person);
        history.setInternalId(dto.getInternalId());
        history.setStartDate(dto.getStartDate());
        history.setEndDate(dto.getEndDate());
        history.setFirstName(dto.getFirstName());
        history.setMiddleName(dto.getMiddleName());
        history.setLastName(dto.getLastName());
        history.setDisplayName(dto.getDisplayName());
        return history;
    }
}
