package ControlCenter.controllers;

import ControlCenter.dto.OrgUnitDTO;
import ControlCenter.dto.OrgUnitHistoryDTO;
import ControlCenter.exception.*;
import ControlCenter.projection.TenantProjection;
import ControlCenter.repository.TenantRepository;
import ControlCenter.service.OrgUnitService;
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
@RequestMapping("/cc/orgunit")
@RequiredArgsConstructor
public class OrgUnitController {

    private static final Logger log = LoggerFactory.getLogger(OrgUnitController.class);
    private final OrgUnitService orgUnitService;
    private final TenantRepository tenantRepository;

    @GetMapping("/orgUnitById")
    public ResponseEntity<OrgUnitDTO> getOrgUnitById(@RequestParam UUID internalId, @RequestParam LocalDate effectiveDate) {
        try {
            Optional<OrgUnitDTO> orgUnit = orgUnitService.getOrgUnitByInternalId(internalId, effectiveDate);
            if (orgUnit.isEmpty()) {
                log.warn("No org unit found by given id");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(orgUnit.get());
        } catch (Exception e) {
            log.error("Error fetching org unit by given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/orgUnitsByTenant")
    public ResponseEntity<List<OrgUnitDTO>> getOrgUnitsByTenant(@RequestParam LocalDate effectiveDate) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            List<OrgUnitDTO> units = orgUnitService.getOrgUnitsByTenant(tenant.getInternalId(), effectiveDate);
            return ResponseEntity.ok(units);
        } catch (Exception e) {
            log.error("Error fetching org units by given tenant id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createOrgUnit")
    public ResponseEntity<OrgUnitDTO> createOrgUnit(@Valid @RequestBody OrgUnitDTO request) {
        try {
            TenantProjection tenant = getTenantIdFromJWTToken();
            request.setTenant(tenant.getInternalId());
            return ResponseEntity.ok(orgUnitService.createOrgUnit(request));
        } catch (OrgUnitNotSavedException e) {
            log.info("Failed to create OrgUnit with external id {}", request.getExternalId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/updateOrgUnit/{orgUnitId}")
    public ResponseEntity<OrgUnitDTO> updateOrgUnit(@PathVariable UUID orgUnitId,
                                                    @RequestParam LocalDate effectiveDate,
                                                    @RequestBody OrgUnitDTO request) {
        try {
            OrgUnitDTO updated = orgUnitService.updateOrgUnit(orgUnitId, effectiveDate, request);
            log.info("OrgUnit with id {} is successfully updated", orgUnitId);
            return ResponseEntity.ok(updated);
        } catch (OrgUnitNotFoundException e) {
            log.error("Failed to upsert OrgUnit: OrgUnit with id: {} could not be found", orgUnitId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (OrgUnitHistoryNotFoundException e) {
            log.error("Effective historical record for OrgUnit with id: {} and effective date: {} could not be found",
                    orgUnitId, effectiveDate);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (OrgUnitWithImmutableUpdateException e) {
            log.error("Not allowed to update field(s) with name(s): {} for OrgUnit with id: {}",
                    e.getFieldNames(), orgUnitId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createOrgUnitHistoricalRecord/{orgUnitId}")
    public ResponseEntity<OrgUnitHistoryDTO> createOrgUnitHistoricalRecord(@PathVariable UUID orgUnitId,
                                                                           @RequestBody OrgUnitHistoryDTO record) {
        try {
            OrgUnitHistoryDTO inserted = orgUnitService.createOrgUnitHistoricalRecord(orgUnitId, record);
            log.info("OrgUnit Historical Record for OrgUnit with id {} is successfully inserted", inserted.getOrgUnitId());
            return ResponseEntity.ok(inserted);
        } catch (OrgUnitNotFoundException e) {
            log.error("Failed to create historical record: OrgUnit with id: {} could not be found", orgUnitId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (OrgUnitHistoryFoundException e) {
            log.error("Failed to create historical record: History with start date: {} already exist", record.getStartDate());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
    }

    @DeleteMapping("/deleteOrgUnitHistoricalRecord/{internalId}")
    public ResponseEntity<OrgUnitDTO> deleteOrgUnitHistory(@PathVariable UUID internalId) {
        try {
            String logMessage = "Historical record for Org Unit with id {} deleted successfully";
            if (orgUnitService.deleteOrgUnitHistoricalRecord(internalId)) {
                logMessage = "Org Unit associated with historical record with id {} is deleted successfully";
            }
            log.info(logMessage, internalId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (OrgUnitNotFoundException e) {
            log.error("Failed to delete historical record with id {}: Org Unit could not be found", internalId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (OrgUnitHistoryNotFoundException e) {
            log.error("Failed to delete historical record with id {}: Historical record could not be found", internalId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteOrgUnit/{orgUnitId}")
    public ResponseEntity<OrgUnitDTO> deleteOrgUnit(@PathVariable UUID orgUnitId) {
        try {
            orgUnitService.deleteOrgUnit(orgUnitId);
            log.info("OrgUnit with id {} is deleted successfully", orgUnitId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (OrgUnitNotFoundException e) {
            log.error("Failed to delete OrgUnit: OrgUnit with id: {} could not be found", orgUnitId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
