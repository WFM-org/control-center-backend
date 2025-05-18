package ControlCenter.entity;

import ControlCenter.dto.PersonDTO;
import ControlCenter.dto.PersonHistoryDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ControlCenter.annotations.ImmutableField;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue
    @Column(name = "internal_id", nullable = false)
    @ImmutableField
    private UUID internalId;

    @Column(name = "tenant", nullable = false)
    @ImmutableField
    private UUID tenant;

    @NotNull(message = "Person Id can not be null")
    @Column(name = "person_id", nullable = false, length = 16)
    private String personId;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "language_pack", nullable = true)
    private LanguagePack languagePack;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PersonHistory> personHistories;

    public void addPersonHistory(PersonHistory history) {
        if (personHistories == null) {
            personHistories = new ArrayList<>();
        }
        personHistories.add(history);
        history.setPerson(this);
    }

    public void removePersonHistory(PersonHistory history) {
        if (personHistories != null) {
            personHistories.remove(history);
        }
    }

    public static Person fromDTO(PersonDTO dto) {
        Person person = new Person();
        person.setPersonId(dto.getPersonId());
        person.setTenant(dto.getTenant());
        if(dto.getLanguagePack() != null) {
            person.setLanguagePack(LanguagePack.fromDTO(dto.getLanguagePack()));
        }
        return person;
    }
}
