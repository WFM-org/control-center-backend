package ControlCenter.crud;

import ControlCenter.exception.EntityNotFoundException;
import ControlCenter.exception.HistoricalEntityAlreadyExistException;
import ControlCenter.exception.HistoricalEntityNotFoundException;
import ControlCenter.exception.ImmutableUpdateException;
import ControlCenter.fieldValidators.ImmutableFieldValidation;
import ControlCenter.service.utils.ServiceUtils;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.*;

/**
 * Overview of Functionality
 *
 * The CrudBuilder handles four main operations:
 *
 * - CREATE: Creates the entity and a corresponding historical record.
 * - READ:   Reads the entity as it existed at a specific point in time (via historical data).
 * - UPDATE: Updates both the entity and its history. Includes immutable field validation and historical context switching.
 * - DELETE: Removes a historical record or, if it's the last one, the entire entity.
 *
 * The entire design is highly generic and powered by functional interfaces,
 * which ensures strong flexibility and decoupling from specific domain logic.
 *
 * Strengths
 *
 * - High reusability: Business logic is encapsulated in injectable functions,
 *   making this component easy to apply across domains.
 * - Temporal modeling: Historical tracking is seamlessly integrated into all operations.
 * - Immutable field validation: Combined with BeanUtils, this makes it easy to
 *   enforce business rules with minimal duplication.
 */

public class CrudBuilder<Entity, HistoricalEntity, DTO, HistoricalDTO> {

    private final Function<UUID, Optional<Entity>> findById;
    private final Function<UUID, Optional<Entity>> findByTenant;
    private final BiFunction<DTO, UUID, HistoricalDTO> fabricateNewHistoricalRecordFromDTO;
    private final BiFunction<HistoricalDTO, UUID, HistoricalDTO> fabricateNewHistoricalRecordFromHistoricalDTO;
    private final Function<Entity, Entity> save;
    private final Function<HistoricalEntity, HistoricalEntity> saveHistory;
    private final Consumer<Entity> delete;
    private final Consumer<HistoricalEntity> deleteHistory;
    private final Function<Entity, List<HistoricalEntity>> getEntityHistories;
    private final Function<DTO, LocalDate> getStartDate;
    private final Function<HistoricalEntity, LocalDate> getEntityHistoryStartDate;
    private final Function<HistoricalDTO, LocalDate> getDTOHistoryStartDate;
    private final Function<Entity, UUID> getId;
    private final Function<DTO, Entity> toEntity;
    private final BiFunction<Entity, LocalDate, DTO> toDTO;
    private final BiFunction<HistoricalDTO, Entity, HistoricalEntity> toHistoricalEntity;
    private final Function<HistoricalEntity, HistoricalDTO> toHistoricalDTO;
    private final Supplier<HistoricalDTO> historicalDTOSupplier;
    private final BiConsumer<HistoricalEntity, Entity> addHistoricalEntity;
    private final Consumer<HistoricalDTO> clearHistoricalId;
    private final EntityManager entityManager;

    public CrudBuilder(Function<UUID, Optional<Entity>> findById, Function<UUID, Optional<Entity>> findByTenant, BiFunction<DTO, UUID, HistoricalDTO> fabricateNewHistoricalRecordFromDTO, BiFunction<HistoricalDTO, UUID, HistoricalDTO> fabricateNewHistoricalRecordFromHistoricalDTO, Function<Entity, Entity> save, Function<HistoricalEntity, HistoricalEntity> saveHistory, Consumer<Entity> delete, Consumer<HistoricalEntity> deleteHistory, Function<Entity, List<HistoricalEntity>> getEntityHistories, Function<DTO, LocalDate> getStartDate, Function<HistoricalEntity, LocalDate> getEntityHistoryStartDate, Function<HistoricalDTO, LocalDate> getDTOHistoryStartDate, Function<Entity, UUID> getId, Function<DTO, Entity> toEntity, BiFunction<Entity, LocalDate, DTO> toDTO, BiFunction<HistoricalDTO, Entity, HistoricalEntity> toHistoricalEntity, Function<HistoricalEntity, HistoricalDTO> toHistoricalDTO, Supplier<HistoricalDTO> historicalDTOSupplier, BiConsumer<HistoricalEntity, Entity> addHistoricalEntity, Consumer<HistoricalDTO> clearHistoricalId, EntityManager entityManager) {
        this.findById = findById;
        this.findByTenant = findByTenant;
        this.fabricateNewHistoricalRecordFromDTO = fabricateNewHistoricalRecordFromDTO;
        this.fabricateNewHistoricalRecordFromHistoricalDTO = fabricateNewHistoricalRecordFromHistoricalDTO;
        this.save = save;
        this.saveHistory = saveHistory;
        this.delete = delete;
        this.deleteHistory = deleteHistory;
        this.getEntityHistories = getEntityHistories;
        this.getStartDate = getStartDate;
        this.getEntityHistoryStartDate = getEntityHistoryStartDate;
        this.getDTOHistoryStartDate = getDTOHistoryStartDate;
        this.getId = getId;
        this.toEntity = toEntity;
        this.toDTO = toDTO;
        this.toHistoricalEntity = toHistoricalEntity;
        this.toHistoricalDTO = toHistoricalDTO;
        this.historicalDTOSupplier = historicalDTOSupplier;
        this.addHistoricalEntity = addHistoricalEntity;
        this.clearHistoricalId = clearHistoricalId;
        this.entityManager = entityManager;
    }

    @Transactional
    public DTO createEntity(DTO dto) {
        // Entity insert
        Entity entity = toEntity.apply(dto);

        // Historical record insert
        HistoricalDTO historicalDTO = fabricateNewHistoricalRecordFromDTO.apply(dto, getId.apply(entity));
        HistoricalEntity historicalEntity = toHistoricalEntity.apply(historicalDTO, entity);
        addHistoricalEntity.accept(historicalEntity, entity);

        Entity newEntity = save.apply(entity);
        refresh(newEntity);
        return toDTO.apply(newEntity, getStartDate.apply(dto));
    }

    public Optional<DTO> readById(UUID id, LocalDate effectiveDate) {
        return findById.apply(id).map(value -> toDTO.apply(value, effectiveDate));
    }

    public List<DTO> readByTenant(UUID tenantId, LocalDate effectiveDate) {
        return findByTenant.apply(tenantId)
                .stream()
                .map(value -> toDTO.apply(value, effectiveDate))
                .toList();
    }

    @Transactional
    public DTO updateEntity(UUID id, LocalDate effectiveDate, DTO update) throws ImmutableUpdateException, HistoricalEntityNotFoundException, EntityNotFoundException {
        // Entity update
        Entity entity = findById.apply(id).orElseThrow(EntityNotFoundException::new);

        Entity updatedEntity = toEntity.apply(update);
        ImmutableFieldValidation.validate(updatedEntity, entity);
        BeanUtils.copyProperties(updatedEntity, entity, ServiceUtils.getNullPropertyNames(updatedEntity));

        // Historical record update
        HistoricalDTO newEntityHistory = historicalDTOSupplier.get();

        if(effectiveDate != null) {
            // Existing historical record:
            // Step 1 - Find the original historical record
            // Step 2 - Delete the original historical record and allow triggers to shift historical records in the database
            List<HistoricalEntity> histories = getEntityHistories.apply(entity);

            HistoricalEntity history = histories
                    .stream()
                    .filter(h -> getEntityHistoryStartDate.apply(h).equals(effectiveDate))
                    .findFirst().orElseThrow(HistoricalEntityNotFoundException::new);

            newEntityHistory = toHistoricalDTO.apply(history);
            clearHistoricalId.accept(newEntityHistory);
            histories.remove(history);
            deleteHistory.accept(history);
        }

        HistoricalDTO sourceUpdateHistoricalDTO = fabricateNewHistoricalRecordFromDTO.apply(update, id);
        BeanUtils.copyProperties(sourceUpdateHistoricalDTO, newEntityHistory, ServiceUtils.getNullPropertyNames(sourceUpdateHistoricalDTO));
        addHistoricalEntity.accept(toHistoricalEntity.apply(newEntityHistory, entity), entity);
        Entity newEntity = save.apply(entity);

        refresh(newEntity);
        return toDTO.apply(newEntity, getDTOHistoryStartDate.apply(newEntityHistory));
    }

    @Transactional
    public HistoricalDTO createHistoricalRecord(UUID parentId, HistoricalDTO record) throws HistoricalEntityAlreadyExistException, EntityNotFoundException {
        Entity parentEntity = findById.apply(parentId).orElseThrow(EntityNotFoundException::new);

        Optional<HistoricalEntity> exist = getEntityHistories.apply(parentEntity)
                .stream()
                .filter(h -> getEntityHistoryStartDate.apply(h).equals(getDTOHistoryStartDate.apply(record)))
                .findFirst();
        if(exist.isPresent()) {
            throw new HistoricalEntityAlreadyExistException();
        }

        HistoricalDTO historicalDTO = fabricateNewHistoricalRecordFromHistoricalDTO.apply(record, parentId);
        HistoricalEntity historicalEntity = toHistoricalEntity.apply(historicalDTO, parentEntity);
        HistoricalEntity newHistoricalEntity = saveHistory.apply(historicalEntity);

        refresh(newHistoricalEntity);
        return toHistoricalDTO.apply(newHistoricalEntity);
    }

    @Transactional
    public boolean deleteHistoricalRecord(UUID parentId, HistoricalDTO record) throws HistoricalEntityNotFoundException, EntityNotFoundException {
        Entity parentEntity = findById.apply(parentId).orElseThrow(EntityNotFoundException::new);

        List<HistoricalEntity> histories = getEntityHistories.apply(parentEntity);

        HistoricalEntity history = histories
                .stream()
                .filter(h -> getEntityHistoryStartDate.apply(h).equals(getDTOHistoryStartDate.apply(record)))
                .findFirst().orElseThrow(HistoricalEntityNotFoundException::new);

        boolean hasDeletedParent = false;
        if(getEntityHistories.apply(parentEntity).size() == 1) {
            delete.accept(parentEntity);
            hasDeletedParent = true;
        } else {
            histories.remove(history);
            deleteHistory.accept(history);
        }

        return hasDeletedParent;
    }

    public void deleteEntity(UUID id) throws EntityNotFoundException {
        Optional<Entity> entity = findById.apply(id);
        if(entity.isEmpty()) throw new EntityNotFoundException();
        delete.accept(entity.get());
    }

    private void refresh(Object object) {
        entityManager.flush();
        entityManager.refresh(object);
    }

}