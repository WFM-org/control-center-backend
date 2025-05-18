package ControlCenter.controllers;

import ControlCenter.dto.EmploymentDTO;
import ControlCenter.dto.EmploymentHistoryDTO;
import ControlCenter.exception.*;
import ControlCenter.projection.TenantProjection;
import ControlCenter.repository.TenantRepository;
import ControlCenter.service.EmploymentService;
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
@RequestMapping("/cc/employment")
@RequiredArgsConstructor
public class EmploymentController {
    private static final Logger log = LoggerFactory.getLogger(EmploymentController.class);
    private final EmploymentService employmentService;
    private final TenantRepository tenantRepository;

    @GetMapping("/employmentById")
    public ResponseEntity<EmploymentDTO> getEmploymentById(@RequestParam UUID internalId, @RequestParam LocalDate effectiveDate) {
        try {
            Optional<EmploymentDTO> employment = employmentService.getEmploymentByInternalId(internalId, effectiveDate);
            if (employment.isEmpty()) {
                log.warn("No employment found by given id");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(employment.get());
        } catch (Exception e) {
            log.error("Error fetching employment by given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employmentsByTenant")
    public ResponseEntity<List<EmploymentDTO>> getEmploymentsByTenant(@RequestParam LocalDate effectiveDate) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            List<EmploymentDTO> employments = employmentService.getEmploymentsByTenant(tenant.getInternalId(), effectiveDate);
            return ResponseEntity.ok(employments);
        } catch (Exception e) {
            log.error("Error fetching employments by given tenant id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createEmployment")
    public ResponseEntity<EmploymentDTO> createEmployment(@Valid @RequestBody EmploymentDTO request) {
        try {
            TenantProjection tenant = getTenantIdFromJWTToken();
            request.setTenant(tenant.getInternalId());
            return ResponseEntity.ok(employmentService.createEmployment(request));
        } catch (EmploymentNotSavedException e) {
            log.info("Failed to create Employment with employee id {}", request.getEmployeeId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/updateEmployment/{employmentId}")
    public ResponseEntity<EmploymentDTO> updateEmployment(@PathVariable UUID employmentId,
                                                          @RequestParam LocalDate effectiveDate,
                                                          @RequestBody EmploymentDTO request) {
        try {
            EmploymentDTO updated = employmentService.updateEmployment(employmentId, effectiveDate, request);
            log.info("Employment with id {} is successfully updated", employmentId);
            return ResponseEntity.ok(updated);
        } catch (EmploymentNotFoundException e) {
            log.error("Failed to update Employment: Employment with id: {} could not be found", employmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (EmploymentHistoryNotFoundException e) {
            log.error("Effective historical record for Employment with id: {} and effective date: {} could not be found",
                    employmentId, effectiveDate);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmploymentWithImmutableUpdateException e) {
            log.error("Not allowed to update field(s): {} for Employment with id: {}",
                    e.getFieldNames(), employmentId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createEmploymentHistoricalRecord/{employmentId}")
    public ResponseEntity<EmploymentHistoryDTO> createEmploymentHistoricalRecord(@PathVariable UUID employmentId,
                                                                                 @RequestBody EmploymentHistoryDTO record) {
        try {
            EmploymentHistoryDTO inserted = employmentService.createEmploymentHistoricalRecord(employmentId, record);
            log.info("Employment Historical Record for Employment with id {} inserted successfully", inserted.getInternalId());
            return ResponseEntity.ok(inserted);
        } catch (EmploymentNotFoundException e) {
            log.error("Failed to create historical record: Employment with id: {} not found", employmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (EmploymentHistoryFoundException e) {
            log.error("Historical record with start date {} already exists", record.getStartDate());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
    }

    @DeleteMapping("/deleteEmploymentHistoricalRecord/{employmentId}")
    public ResponseEntity<EmploymentDTO> deleteEmploymentHistory(@PathVariable UUID employmentId,
                                                                 @RequestBody EmploymentHistoryDTO record) {
        try {
            String logMessage = "Historical record assigned to Employment with id {} deleted successfully";
            if (employmentService.deleteEmploymentHistoricalRecord(employmentId, record)) {
                logMessage = "Employment with id {} and its historical data deleted successfully";
            }
            log.info(logMessage, employmentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EmploymentNotFoundException e) {
            log.error("Failed to delete historical record: Employment with id: {} not found", employmentId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EmploymentHistoryNotFoundException e) {
            log.error("Failed to delete historical record: Start date {} not found", record.getStartDate());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteEmployment/{employmentId}")
    public ResponseEntity<EmploymentDTO> deleteEmployment(@PathVariable UUID employmentId) {
        try {
            employmentService.deleteEmployment(employmentId);
            log.info("Employment with id {} deleted successfully", employmentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EmploymentNotFoundException e) {
            log.error("Failed to delete Employment: id {} not found", employmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
