package ControlCenter.service;

import ControlCenter.builder.HistoricalDataManagementBuilder;
import ControlCenter.dto.OrgUnitDTO;
import ControlCenter.dto.OrgUnitHistoryDTO;
import ControlCenter.entity.OrgUnit;
import ControlCenter.entity.OrgUnitHistory;
import ControlCenter.exception.*;
import ControlCenter.repository.OrgUnitHistoryRepository;
import ControlCenter.repository.OrgUnitRepository;
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
public class OrgUnitService {

    private final HistoricalDataManagementBuilder<OrgUnit, OrgUnitHistory, OrgUnitDTO, OrgUnitHistoryDTO> builder;

    public OrgUnitService(OrgUnitRepository orgUnitRepository,
                          OrgUnitHistoryRepository orgUnitHistoryRepository,
                          EntityManager entityManager) {

        this.builder = new HistoricalDataManagementBuilder<>(
                orgUnitRepository::findOrgUnitById,
                orgUnitRepository::findOrgUnitsByTenantId,
                (dto, id) -> new OrgUnitHistoryDTO(null, id,
                        dto.getStartDate(), dto.getName(), dto.getParentUnitId()),
                (dto, id) -> new OrgUnitHistoryDTO(null, id,
                        dto.getStartDate(), dto.getName(), dto.getParentUnitId()),
                orgUnitRepository::save,
                orgUnitHistoryRepository::save,
                orgUnitRepository::delete,
                orgUnitHistoryRepository::delete,
                OrgUnit::getOrgUnitHistories,
                OrgUnitDTO::getStartDate,
                OrgUnitHistory::getStartDate,
                OrgUnitHistoryDTO::getStartDate,
                OrgUnit::getInternalId,
                OrgUnit::fromDTO,
                OrgUnitDTO::fromEntity,
                OrgUnitHistory::fromDTO,
                OrgUnitHistoryDTO::fromEntity,
                OrgUnitHistoryDTO::new,
                (h, p) -> p.addOrgUnitHistory(h),
                h -> h.setInternalId(null),
                entityManager
        );
    }

    public Optional<OrgUnitDTO> getOrgUnitByInternalId(UUID internalId, LocalDate effectiveDate) {
        return builder.readById(internalId, effectiveDate);
    }

    public List<OrgUnitDTO> getOrgUnitsByTenant(UUID tenantId, LocalDate effectiveDate) {
        return builder.readByTenant(tenantId, effectiveDate);
    }

    public void deleteOrgUnit(UUID internalId) throws OrgUnitNotFoundException {
        try {
            builder.deleteEntity(internalId);
        } catch (EntityNotFoundException e) {
            throw new OrgUnitNotFoundException();
        }
    }

    @Transactional
    public OrgUnitDTO createOrgUnit(OrgUnitDTO orgUnit) throws OrgUnitNotSavedException {
        return builder.createEntity(orgUnit);
    }

    @Transactional
    public OrgUnitDTO updateOrgUnit(UUID internalId, LocalDate effectiveDate, OrgUnitDTO update)
            throws OrgUnitHistoryNotFoundException, OrgUnitWithImmutableUpdateException, OrgUnitNotFoundException {
        try {
            return builder.updateEntity(internalId, effectiveDate, update);
        } catch (EntityNotFoundException e) {
            throw new OrgUnitNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new OrgUnitHistoryNotFoundException();
        } catch (ImmutableUpdateException e) {
            throw new OrgUnitWithImmutableUpdateException(e.getFieldNames());
        }
    }

    @Transactional
    public OrgUnitHistoryDTO createOrgUnitHistoricalRecord(UUID parentId, OrgUnitHistoryDTO record)
            throws OrgUnitNotFoundException, OrgUnitHistoryFoundException {
        try {
            return builder.createHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new OrgUnitNotFoundException();
        } catch (HistoricalEntityAlreadyExistException e) {
            throw new OrgUnitHistoryFoundException();
        }
    }

    @Transactional
    public Boolean deleteOrgUnitHistoricalRecord(UUID parentId, OrgUnitHistoryDTO record)
            throws OrgUnitNotFoundException, OrgUnitHistoryNotFoundException {
        try {
            return builder.deleteHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new OrgUnitNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new OrgUnitHistoryNotFoundException();
        }
    }
}
