package ControlCenter.service;

import ControlCenter.builder.HistoricalDataManagementBuilder;
import ControlCenter.dto.CostCenterDTO;
import ControlCenter.dto.CostCenterHistoryDTO;
import ControlCenter.entity.CostCenter;
import ControlCenter.entity.CostCenterHistory;
import ControlCenter.exception.*;
import ControlCenter.repository.CostCenterRepository;
import ControlCenter.repository.CostCenterHistoryRepository;
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
public class CostCenterService {

    private final HistoricalDataManagementBuilder<CostCenter, CostCenterHistory, CostCenterDTO, CostCenterHistoryDTO> builder;

    public CostCenterService(CostCenterRepository costCenterRepository,
                             CostCenterHistoryRepository costCenterHistoryRepository,
                             EntityManager entityManager) {

        this.builder = new HistoricalDataManagementBuilder<>(
                costCenterRepository::findCostCenterById,
                costCenterRepository::findCostCentersByTenantId,
                (dto, id) -> new CostCenterHistoryDTO(null, id,
                        dto.getStartDate(), dto.getName(), dto.getParentUnitId()),
                (dto, id) -> new CostCenterHistoryDTO(null, id,
                        dto.getStartDate(), dto.getName(), dto.getParentUnitId()),
                costCenterRepository::save,
                costCenterHistoryRepository::save,
                costCenterRepository::delete,
                costCenterHistoryRepository::delete,
                CostCenter::getCostCenterHistories,
                CostCenterDTO::getStartDate,
                CostCenterHistory::getStartDate,
                CostCenterHistoryDTO::getStartDate,
                CostCenter::getInternalId,
                CostCenter::fromDTO,
                CostCenterDTO::fromEntity,
                CostCenterHistory::fromDTO,
                CostCenterHistoryDTO::fromEntity,
                CostCenterHistoryDTO::new,
                (ch, cc) -> cc.addCostCenterHistory(ch),
                (ch) -> ch.setInternalId(null),
                entityManager
        );
    }

    public Optional<CostCenterDTO> getCostCenterByInternalId(UUID internalId, LocalDate effectiveDate) {
        return builder.readById(internalId, effectiveDate);
    }

    public List<CostCenterDTO> getCostCentersByTenant(UUID tenantId, LocalDate effectiveDate) {
        return builder.readByTenant(tenantId, effectiveDate);
    }

    public void deleteCostCenter(UUID internalId) throws CostCenterNotFoundException {
        try {
            builder.deleteEntity(internalId);
        } catch (EntityNotFoundException e) {
            throw new CostCenterNotFoundException();
        }
    }

    @Transactional
    public CostCenterDTO createCostCenter(CostCenterDTO costCenter) throws CostCenterNotSavedException {
        return builder.createEntity(costCenter);
    }

    @Transactional
    public CostCenterDTO updateCostCenter(UUID internalId, LocalDate effectiveDate, CostCenterDTO update) throws CostCenterHistoryNotFoundException, CostCenterWithImmutableUpdateException, CostCenterNotFoundException {
        try {
            return builder.updateEntity(internalId, effectiveDate, update);
        } catch (EntityNotFoundException e) {
            throw new CostCenterNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new CostCenterHistoryNotFoundException();
        } catch (ImmutableUpdateException e) {
            throw new CostCenterWithImmutableUpdateException(e.getFieldNames());
        }
    }

    @Transactional
    public CostCenterHistoryDTO createCostCenterHistoricalRecord(UUID parentId, CostCenterHistoryDTO record) throws CostCenterNotFoundException, CostCenterHistoryFoundException {
        try {
            return builder.createHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new CostCenterNotFoundException();
        } catch (HistoricalEntityAlreadyExistException e) {
            throw new CostCenterHistoryFoundException();
        }
    }

    @Transactional
    public Boolean deleteCostCenterHistoricalRecord(UUID parentId, CostCenterHistoryDTO record) throws CostCenterNotFoundException, CostCenterHistoryNotFoundException {
        try {
            return builder.deleteHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new CostCenterNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new CostCenterHistoryNotFoundException();
        }
    }
}
