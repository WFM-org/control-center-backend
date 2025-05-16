package ControlCenter.crud;

import ControlCenter.exception.HistoricalEntityAlreadyExistException;
import ControlCenter.exception.HistoricalEntityNotFoundException;
import ControlCenter.exception.EntityNotFoundException;
import ControlCenter.exception.ImmutableUpdateException;
import ControlCenter.fieldValidators.ImmutableFieldValidation;
import ControlCenter.service.utils.ServiceUtils;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CrudBuilder<Entity, HistoricalEntity, DTO, HistoricalDTO, HistoricalId> {

    private final Function<UUID, Optional<Entity>> findById;
    private final Function<UUID, Optional<Entity>> findByTenant;
    private final BiFunction<DTO, UUID, HistoricalDTO> fabricateHistoricalRecordFromDTO;
    private final BiFunction<HistoricalDTO, UUID, HistoricalDTO> fabricateHistoricalRecordFromHistoricalDTO;
    private final Function<Entity, Entity> save;
    private final Consumer<Entity> delete;
    private final Consumer<HistoricalId> deleteHistoricalEntityByEmbeddedId;
    private final Function<Entity, List<HistoricalEntity>> getEntityHistories;
    private final BiFunction<List<HistoricalEntity>, Entity, Entity> setEntityHistories;
    private final Function<DTO, LocalDate> getStartDate;
    private final Function<HistoricalEntity, LocalDate> getEntityHistoryStartDate;
    private final Function<HistoricalDTO, LocalDate> getDTOHistoryStartDate;
    private final Function<HistoricalEntity, HistoricalId> getHistoricalId;
    private final Function<Entity, UUID> getId;
    private final Function<DTO, Entity> toEntity;
    private final BiFunction<Entity, LocalDate, DTO> toDTO;
    private final Function<HistoricalDTO, HistoricalEntity> toHistoricalEntity;
    private final Function<HistoricalEntity, HistoricalDTO> toHistoricalDTO;
    private final Supplier<HistoricalDTO> historicalDTOSupplier;
    private final EntityManager entityManager;

    public CrudBuilder(Function<UUID, Optional<Entity>> findById,
                       Function<UUID, Optional<Entity>> findByTenant,
                       BiFunction<DTO, UUID, HistoricalDTO> fabricateHistoricalRecordFromDTO,
                       BiFunction<HistoricalDTO, UUID, HistoricalDTO> fabricateHistoricalRecordFromHistoricalDTO,
                       Function<Entity, Entity> save,
                       Consumer<Entity> delete,
                       Consumer<HistoricalId> deleteHistoricalEntityByEmbeddedId,
                       Function<Entity, List<HistoricalEntity>> getEntityHistories,
                       BiFunction<List<HistoricalEntity>, Entity, Entity> setEntityHistories,
                       Function<DTO, LocalDate> getStartDate,
                       Function<HistoricalEntity, LocalDate> getEntityHistoryStartDate,
                       Function<HistoricalDTO, LocalDate> getDTOHistoryStartDate,
                       Function<HistoricalEntity, HistoricalId> getHistoricalId,
                       Function<Entity, UUID> getId,
                       Function<DTO, Entity> toEntity,
                       BiFunction<Entity, LocalDate, DTO> toDTO,
                       Function<HistoricalDTO, HistoricalEntity> toHistoricalEntity,
                       Function<HistoricalEntity, HistoricalDTO> toHistoricalDTO,
                       Supplier<HistoricalDTO> historicalDTOSupplier,
                       EntityManager entityManager) {

        this.findById = findById;
        this.findByTenant = findByTenant;
        this.fabricateHistoricalRecordFromDTO = fabricateHistoricalRecordFromDTO;
        this.fabricateHistoricalRecordFromHistoricalDTO = fabricateHistoricalRecordFromHistoricalDTO;
        this.save = save;
        this.delete = delete;
        this.deleteHistoricalEntityByEmbeddedId = deleteHistoricalEntityByEmbeddedId;
        this.getEntityHistories = getEntityHistories;
        this.setEntityHistories = setEntityHistories;
        this.getStartDate = getStartDate;
        this.getEntityHistoryStartDate = getEntityHistoryStartDate;
        this.getDTOHistoryStartDate = getDTOHistoryStartDate;
        this.getHistoricalId = getHistoricalId;
        this.getId = getId;
        this.toEntity = toEntity;
        this.toDTO = toDTO;
        this.toHistoricalEntity = toHistoricalEntity;
        this.toHistoricalDTO = toHistoricalDTO;
        this.historicalDTOSupplier = historicalDTOSupplier;
        this.entityManager = entityManager;
    }

    @Transactional
    public DTO createEntity(DTO dto) {
        // Entity insert
        Entity entity = toEntity.apply(dto);
        Entity newEntity = save.apply(entity);

        // Historical record insert
        HistoricalDTO historicalDTO = fabricateHistoricalRecordFromDTO.apply(dto, getId.apply(entity));
        HistoricalEntity historicalEntity = toHistoricalEntity.apply(historicalDTO);
        List<HistoricalEntity> mutableHistoricalEntityList = new ArrayList<>(List.of(historicalEntity));
        newEntity = setEntityHistories.apply(mutableHistoricalEntityList, newEntity);
        Entity inserted = save.apply(newEntity);

        // TODO: Fix, does not work, consider add this in a saveAndRefresh or somehow keep everything in same transactional
        //entityManager.flush();
        //entityManager.refresh(inserted);

        return toDTO.apply(inserted, getStartDate.apply(dto));
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

    public DTO updateEntity(UUID id, LocalDate effectiveDate, DTO update) throws ImmutableUpdateException, HistoricalEntityNotFoundException, EntityNotFoundException {
        // Entity update
        Entity entity = findById.apply(id).orElseThrow(EntityNotFoundException::new);

        Entity updatedEntity = toEntity.apply(update);
        ImmutableFieldValidation.validate(updatedEntity, entity);
        BeanUtils.copyProperties(updatedEntity, entity, ServiceUtils.getNullPropertyNames(updatedEntity));
        Entity newEntity = save.apply(entity);

        // Historical record update
        HistoricalDTO newEntityHistory = historicalDTOSupplier.get();

        if(effectiveDate != null) {
            // Existing historical record:
            // Step 1 - Find the original historical record
            // Step 2 - Delete the original historical record and allow triggers to shift historical records in the database
            HistoricalEntity history = getEntityHistories.apply(newEntity)
                    .stream()
                    .filter(h -> getEntityHistoryStartDate.apply(h).equals(effectiveDate))
                    .findFirst().orElseThrow(HistoricalEntityNotFoundException::new);

            newEntityHistory = toHistoricalDTO.apply(history);

            deleteHistoricalEntityByEmbeddedId.accept(getHistoricalId.apply(history));
            getEntityHistories.apply(newEntity).remove(history);
        }

        HistoricalDTO sourceUpdateHistoricalDTO = fabricateHistoricalRecordFromDTO.apply(update, id);
        BeanUtils.copyProperties(sourceUpdateHistoricalDTO, newEntityHistory, ServiceUtils.getNullPropertyNames(sourceUpdateHistoricalDTO));
        getEntityHistories.apply(newEntity).add(toHistoricalEntity.apply(newEntityHistory));
        save.apply(newEntity);

        // TODO: Fix, does not work, consider add this in a saveAndRefresh or somehow keep everything in same transactional
        //entityManager.flush();
        //entityManager.refresh(newEntity);

        return toDTO.apply(newEntity, getDTOHistoryStartDate.apply(newEntityHistory));
    }

    public HistoricalDTO createHistoricalRecord(UUID parentId, HistoricalDTO record) throws HistoricalEntityAlreadyExistException, EntityNotFoundException {
        Entity parentEntity = findById.apply(parentId).orElseThrow(EntityNotFoundException::new);

        Optional<HistoricalEntity> exist = getEntityHistories.apply(parentEntity)
                .stream()
                .filter(h -> getEntityHistoryStartDate.apply(h).equals(getDTOHistoryStartDate.apply(record)))
                .findFirst();
        if(exist.isPresent()) {
            throw new HistoricalEntityAlreadyExistException();
        }

        HistoricalDTO newHistoricalDTO = fabricateHistoricalRecordFromHistoricalDTO.apply(record, parentId);
        HistoricalEntity newHistoricalEntity = toHistoricalEntity.apply(newHistoricalDTO);
        getEntityHistories.apply(parentEntity).add(newHistoricalEntity);
        save.apply(parentEntity);

        return newHistoricalDTO;
    }

    public boolean deleteHistoricalRecord(UUID parentId, HistoricalDTO record) throws HistoricalEntityNotFoundException, EntityNotFoundException {
        Entity parentEntity = findById.apply(parentId).orElseThrow(EntityNotFoundException::new);

        HistoricalEntity history = getEntityHistories.apply(parentEntity)
                .stream()
                .filter(h -> getEntityHistoryStartDate.apply(h).equals(getDTOHistoryStartDate.apply(record)))
                .findFirst().orElseThrow(HistoricalEntityNotFoundException::new);

        boolean hasDeletedParent = false;
        if(getEntityHistories.apply(parentEntity).size() == 1) {
            delete.accept(parentEntity);
            hasDeletedParent = true;
        } else {
            deleteHistoricalEntityByEmbeddedId.accept(getHistoricalId.apply(history));
        }

        return hasDeletedParent;
    }

    public void deleteEntity(UUID id) throws EntityNotFoundException {
        Optional<Entity> entity = findById.apply(id);
        if(entity.isEmpty()) throw new EntityNotFoundException();
        delete.accept(entity.get());
    }

}