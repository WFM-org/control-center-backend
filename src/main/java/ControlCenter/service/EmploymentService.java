package ControlCenter.service;

import ControlCenter.builder.HistoricalDataManagementBuilder;
import ControlCenter.dto.EmploymentDTO;
import ControlCenter.dto.EmploymentHistoryDTO;
import ControlCenter.entity.Employment;
import ControlCenter.entity.EmploymentHistory;
import ControlCenter.exception.*;
import ControlCenter.repository.EmploymentHistoryRepository;
import ControlCenter.repository.EmploymentRepository;
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
public class EmploymentService {

    private final HistoricalDataManagementBuilder<Employment, EmploymentHistory, EmploymentDTO, EmploymentHistoryDTO> builder;
    private final EmploymentRepository employmentRepository;

    public EmploymentService(EmploymentRepository employmentRepository,
                             EmploymentHistoryRepository employmentHistoryRepository,
                             EntityManager entityManager) {
        this.employmentRepository = employmentRepository;
        this.builder = new HistoricalDataManagementBuilder<>(
                employmentRepository::findEmploymentById,
                employmentRepository::findEmploymentsByTenantId,
                (dto, id) -> new EmploymentHistoryDTO(null, id,
                        dto.getStartDate(), dto.getEndDate(), dto.getEvent(), dto.getEmployeeStatus(),
                        dto.getCompanyId(), dto.getTimezone(), dto.getManagerId(), dto.getHrId(), dto.getOrgUnitId(),
                        dto.getCostCenterId()),
                (dto, id) -> new EmploymentHistoryDTO(null, id,
                        dto.getStartDate(), dto.getEndDate(), dto.getEvent(), dto.getEmployeeStatus(),
                        dto.getCompanyId(), dto.getTimezone(), dto.getManagerId(), dto.getHrId(), dto.getOrgUnitId(),
                        dto.getCostCenterId()),
                employmentRepository::save,
                employmentHistoryRepository::save,
                employmentRepository::delete,
                employmentHistoryRepository::delete,
                Employment::getEmploymentHistories,
                EmploymentDTO::getStartDate,
                EmploymentHistory::getStartDate,
                EmploymentHistory::getEndDate,
                EmploymentHistoryDTO::getStartDate,
                Employment::getInternalId,
                Employment::fromDTO,
                EmploymentDTO::fromEntity,
                EmploymentHistory::fromDTO,
                EmploymentHistoryDTO::fromEntity,
                EmploymentHistoryDTO::new,
                (eh, e) -> e.addEmploymentHistory(eh),
                (eh) -> eh.setInternalId(null),
                entityManager
        );
    }

    public Boolean isActive(UUID internalId, LocalDate effectiveDate) {
        return employmentRepository.isActive(internalId, effectiveDate);
    }

    public Optional<EmploymentDTO> getEmploymentByInternalId(UUID internalId, LocalDate effectiveDate) {
        return builder.readById(internalId, effectiveDate);
    }

    public List<EmploymentDTO> getEmploymentsByTenant(UUID tenantId, LocalDate effectiveDate) {
        return builder.readByTenant(tenantId, effectiveDate);
    }

    public void deleteEmployment(UUID internalId) throws EmploymentNotFoundException {
        try {
            builder.deleteEntity(internalId);
        } catch (EntityNotFoundException e) {
            throw new EmploymentNotFoundException();
        }
    }

    @Transactional
    public EmploymentDTO createEmployment(EmploymentDTO employment) throws EmploymentNotSavedException {
        return builder.createEntity(employment);
    }

    @Transactional
    public EmploymentDTO updateEmployment(UUID internalId, LocalDate effectiveDate, EmploymentDTO update)
            throws EmploymentHistoryNotFoundException, EmploymentWithImmutableUpdateException, EmploymentNotFoundException {
        try {
            return builder.updateEntity(internalId, effectiveDate, update);
        } catch (EntityNotFoundException e) {
            throw new EmploymentNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new EmploymentHistoryNotFoundException();
        } catch (ImmutableUpdateException e) {
            throw new EmploymentWithImmutableUpdateException(e.getFieldNames());
        }
    }

    @Transactional
    public EmploymentHistoryDTO createEmploymentHistoricalRecord(UUID parentId, EmploymentHistoryDTO record)
            throws EmploymentNotFoundException, EmploymentHistoryFoundException {
        try {
            return builder.createHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new EmploymentNotFoundException();
        } catch (HistoricalEntityAlreadyExistException e) {
            throw new EmploymentHistoryFoundException();
        }
    }

    @Transactional
    public Boolean deleteEmploymentHistoricalRecord(UUID parentId, EmploymentHistoryDTO record)
            throws EmploymentNotFoundException, EmploymentHistoryNotFoundException {
        try {
            return builder.deleteHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new EmploymentNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new EmploymentHistoryNotFoundException();
        }
    }
}
