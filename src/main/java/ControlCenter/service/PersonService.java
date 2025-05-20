package ControlCenter.service;

import ControlCenter.builder.HistoricalDataManagementBuilder;
import ControlCenter.dto.PersonDTO;
import ControlCenter.dto.PersonHistoryDTO;
import ControlCenter.entity.Person;
import ControlCenter.entity.PersonHistory;
import ControlCenter.exception.*;
import ControlCenter.repository.PersonHistoryRepository;
import ControlCenter.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PersonService {

    private final HistoricalDataManagementBuilder<Person, PersonHistory, PersonDTO, PersonHistoryDTO> builder;

    public PersonService(PersonRepository personRepository,
                         PersonHistoryRepository personHistoryRepository,
                         EntityManager entityManager) {

        this.builder = new HistoricalDataManagementBuilder<>(
                personRepository::findPersonById,
                personRepository::findPersonsByTenantId,
                (dto, id) -> new PersonHistoryDTO(null, id,
                        dto.getStartDate(), dto.getEndDate(),
                        dto.getFirstName(), dto.getMiddleName(), dto.getLastName(), dto.getDisplayName(), dto.getLanguagePack()),
                (dto, id) -> new PersonHistoryDTO(null, id,
                        dto.getStartDate(), dto.getEndDate(),
                        dto.getFirstName(), dto.getMiddleName(), dto.getLastName(), dto.getDisplayName(), dto.getLanguagePack()),
                personRepository::save,
                personHistoryRepository::save,
                personRepository::delete,
                personHistoryRepository::delete,
                Person::getPersonHistories,
                PersonDTO::getStartDate,
                PersonHistory::getStartDate,
                PersonHistory::getEndDate,
                PersonHistoryDTO::getStartDate,
                Person::getInternalId,
                Person::fromDTO,
                PersonDTO::fromEntity,
                PersonHistory::fromDTO,
                PersonHistoryDTO::fromEntity,
                PersonHistoryDTO::new,
                (ph, p) -> p.addPersonHistory(ph),
                (ph) -> ph.setInternalId(null),
                entityManager
        );
    }

    public Optional<PersonDTO> getPersonByInternalId(UUID internalId, LocalDate effectiveDate) {
        return builder.readById(internalId, effectiveDate);
    }

    public List<PersonDTO> getPersonsByTenant(UUID tenantId, LocalDate effectiveDate) {
        return builder.readByTenant(tenantId, effectiveDate);
    }

    public void deletePerson(UUID internalId) throws PersonNotFoundException {
        try {
            builder.deleteEntity(internalId);
        } catch (EntityNotFoundException e) {
            throw new PersonNotFoundException();
        }
    }

    @Transactional
    public PersonDTO createPerson(PersonDTO person) throws PersonNotSavedException {
        return builder.createEntity(person);
    }

    @Transactional
    public PersonDTO updatePerson(UUID internalId, LocalDate effectiveDate, PersonDTO update) throws PersonHistoryNotFoundException, PersonWithImmutableUpdateException, PersonNotFoundException {
        try {
            return builder.updateEntity(internalId, effectiveDate, update);
        } catch (EntityNotFoundException e) {
            throw new PersonNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new PersonHistoryNotFoundException();
        } catch (ImmutableUpdateException e) {
            throw new PersonWithImmutableUpdateException(e.getFieldNames());
        }
    }

    @Transactional
    public PersonHistoryDTO createPersonHistoricalRecord(UUID parentId, PersonHistoryDTO record) throws PersonNotFoundException, PersonHistoryFoundException {
        try {
            return builder.createHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new PersonNotFoundException();
        } catch (HistoricalEntityAlreadyExistException e) {
            throw new PersonHistoryFoundException();
        }
    }

    @Transactional
    public Boolean deletePersonHistoricalRecord(UUID parentId, PersonHistoryDTO record) throws PersonNotFoundException, PersonHistoryNotFoundException {
        try {
            return builder.deleteHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new PersonNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new PersonHistoryNotFoundException();
        }
    }
}
