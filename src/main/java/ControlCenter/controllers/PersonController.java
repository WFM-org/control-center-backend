package ControlCenter.controllers;

import ControlCenter.dto.PersonDTO;
import ControlCenter.dto.PersonHistoryDTO;
import ControlCenter.exception.*;
import ControlCenter.projection.TenantProjection;
import ControlCenter.repository.TenantRepository;
import ControlCenter.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cc/person")
@RequiredArgsConstructor
public class PersonController {
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    private final TenantRepository tenantRepository;

    @GetMapping("/personById")
    public ResponseEntity<PersonDTO> getPersonById(@RequestParam UUID internalId, @RequestParam LocalDate effectiveDate) {
        try {
            Optional<PersonDTO> person = personService.getPersonByInternalId(internalId, effectiveDate);
            if (person.isEmpty()) {
                log.warn("No persons found by given id");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(person.get());
        } catch (Exception e) {
            log.error("Error fetching persons by given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/personsByTenant")
    public ResponseEntity<List<PersonDTO>> getPersonsByTenant(@RequestParam LocalDate effectiveDate) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            List<PersonDTO> persons = personService.getPersonsByTenant(tenant.getInternalId(), effectiveDate);
            return ResponseEntity.ok(persons);
        } catch (Exception e) {
            log.error("Error fetching persons by given tenant id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createPerson")
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO request) {
        try {
            TenantProjection tenant = getTenantIdFromJWTToken();
            request.setTenant(tenant.getInternalId());
            return ResponseEntity.ok(personService.createPerson(request));
        } catch (PersonNotSavedException e) {
            log.info("Failed to create Person with first and last name id {}",
                    request.getFirstName().concat(" ").concat(request.getLastName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/updatePerson/{personId}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable UUID personId,
                                                  @RequestParam LocalDate effectiveDate,
                                                  @RequestBody PersonDTO request) {
        try {
            PersonDTO updated = personService.updatePerson(personId, effectiveDate, request);
            log.info("Person with id {} is successfully updated", personId);
            return ResponseEntity.ok(updated);
        } catch (PersonNotFoundException e) {
            log.error("Failed to upsert Person: Person with id: {} could not be found", personId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (PersonHistoryNotFoundException e) {
            log.error("Effective historical record for Person with id: {} and effective date: {} could not be found",
                    personId, effectiveDate);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (PersonWithImmutableUpdateException e) {
            log.error("Not allowed to update field(s) with name(s): {} for Person with id: {}",
                    e.getFieldNames(), personId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createPersonHistoricalRecord/{personId}")
    public ResponseEntity<PersonHistoryDTO> createPersonHistoricalRecord(@PathVariable UUID personId,
                                                                         @RequestBody PersonHistoryDTO record) {
        try {
            PersonHistoryDTO inserted = personService.createPersonHistoricalRecord(personId, record);
            log.info("Person Historical Record for Person with id {} is successfully inserted", inserted.getPersonId());
            return ResponseEntity.ok(inserted);
        } catch (PersonNotFoundException e) {
            log.error("Failed to create historical record: Person with id: {} could not be found", personId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (PersonHistoryFoundException e) {
            log.error("Failed to create historical record: History with start date: {} already exist", record.getStartDate());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
    }

    @DeleteMapping("/deletePersonHistoricalRecord/{personId}")
    public ResponseEntity<PersonDTO> deletePersonHistory(@PathVariable UUID personId,
                                                         @RequestBody PersonHistoryDTO record) {
        try {
            String logMessage = "Historical record with assigned to Person with id {} is deleted successfully";
            if(personService.deletePersonHistoricalRecord(personId, record)) {
                logMessage = "Person with with id {} and historical data is deleted successfully";
            }
            log.info(logMessage, personId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PersonNotFoundException e) {
            log.error("Failed to delete historical record: Person with id: {} could not be found", personId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (PersonHistoryNotFoundException e) {
            log.error("Failed to delete historical record: History with start date: {} could not be found", record.getStartDate());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deletePerson/{personId}")
    public ResponseEntity<PersonDTO> deletePerson(@PathVariable UUID personId) {
        try {
            personService.deletePerson(personId);
            log.info("Person with id {} is deleted successfully", personId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PersonNotFoundException e) {
            log.error("Failed to delete Person: Person with id: {} could not be found", personId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
