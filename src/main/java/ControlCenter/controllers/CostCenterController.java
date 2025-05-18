package ControlCenter.controllers;

import ControlCenter.dto.CostCenterDTO;
import ControlCenter.dto.CostCenterHistoryDTO;
import ControlCenter.exception.*;
import ControlCenter.projection.TenantProjection;
import ControlCenter.repository.TenantRepository;
import ControlCenter.service.CostCenterService;
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
@RequestMapping("/cc/costCenter")
@RequiredArgsConstructor
public class CostCenterController {
    private static final Logger log = LoggerFactory.getLogger(CostCenterController.class);
    private final CostCenterService costCenterService;
    private final TenantRepository tenantRepository;

    @GetMapping("/costCenterById")
    public ResponseEntity<CostCenterDTO> getCostCenterById(@RequestParam UUID internalId, @RequestParam LocalDate effectiveDate) {
        try {
            Optional<CostCenterDTO> costCenter = costCenterService.getCostCenterByInternalId(internalId, effectiveDate);
            if (costCenter.isEmpty()) {
                log.warn("No cost centers found by given id");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(costCenter.get());
        } catch (Exception e) {
            log.error("Error fetching cost centers by given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/costCentersByTenant")
    public ResponseEntity<List<CostCenterDTO>> getCostCentersByTenant(@RequestParam LocalDate effectiveDate) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            List<CostCenterDTO> costCenters = costCenterService.getCostCentersByTenant(tenant.getInternalId(), effectiveDate);
            return ResponseEntity.ok(costCenters);
        } catch (Exception e) {
            log.error("Error fetching cost centers by given tenant id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createCostCenter")
    public ResponseEntity<CostCenterDTO> createCostCenter(@Valid @RequestBody CostCenterDTO request) {
        try {
            TenantProjection tenant = getTenantIdFromJWTToken();
            request.setTenant(tenant.getInternalId());
            return ResponseEntity.ok(costCenterService.createCostCenter(request));
        } catch (CostCenterNotSavedException e) {
            log.info("Failed to create CostCenter with external id {}", request.getExternalId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/updateCostCenter/{costCenterId}")
    public ResponseEntity<CostCenterDTO> updateCostCenter(@PathVariable UUID costCenterId,
                                                          @RequestParam LocalDate effectiveDate,
                                                          @RequestBody CostCenterDTO request) {
        try {
            CostCenterDTO updated = costCenterService.updateCostCenter(costCenterId, effectiveDate, request);
            log.info("CostCenter with id {} is successfully updated", costCenterId);
            return ResponseEntity.ok(updated);
        } catch (CostCenterNotFoundException e) {
            log.error("CostCenter with id: {} could not be found", costCenterId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CostCenterHistoryNotFoundException e) {
            log.error("Effective historical record for CostCenter with id: {} and effective date: {} could not be found",
                    costCenterId, effectiveDate);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (CostCenterWithImmutableUpdateException e) {
            log.error("Not allowed to update field(s): {} for CostCenter with id: {}",
                    e.getFieldNames(), costCenterId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createCostCenterHistoricalRecord/{costCenterId}")
    public ResponseEntity<CostCenterHistoryDTO> createCostCenterHistoricalRecord(@PathVariable UUID costCenterId,
                                                                                 @RequestBody CostCenterHistoryDTO record) {
        try {
            CostCenterHistoryDTO inserted = costCenterService.createCostCenterHistoricalRecord(costCenterId, record);
            log.info("Historical record for CostCenter with id {} inserted successfully", inserted.getCostCenterId());
            return ResponseEntity.ok(inserted);
        } catch (CostCenterNotFoundException e) {
            log.error("CostCenter with id: {} not found", costCenterId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CostCenterHistoryFoundException e) {
            log.error("Historical record with start date: {} already exists", record.getStartDate());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
    }

    @DeleteMapping("/deleteCostCenterHistoricalRecord/{costCenterId}")
    public ResponseEntity<CostCenterDTO> deleteCostCenterHistory(@PathVariable UUID costCenterId,
                                                                 @RequestBody CostCenterHistoryDTO record) {
        try {
            String logMessage = "Historical record for CostCenter with id {} deleted successfully";
            if (costCenterService.deleteCostCenterHistoricalRecord(costCenterId, record)) {
                logMessage = "CostCenter with id {} and its historical data deleted successfully";
            }
            log.info(logMessage, costCenterId);
            return ResponseEntity.ok().build();
        } catch (CostCenterNotFoundException e) {
            log.error("CostCenter with id: {} not found", costCenterId);
            return ResponseEntity.badRequest().build();
        } catch (CostCenterHistoryNotFoundException e) {
            log.error("Historical record with start date: {} not found", record.getStartDate());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteCostCenter/{costCenterId}")
    public ResponseEntity<CostCenterDTO> deleteCostCenter(@PathVariable UUID costCenterId) {
        try {
            costCenterService.deleteCostCenter(costCenterId);
            log.info("CostCenter with id {} deleted successfully", costCenterId);
            return ResponseEntity.ok().build();
        } catch (CostCenterNotFoundException e) {
            log.error("CostCenter with id: {} not found", costCenterId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
