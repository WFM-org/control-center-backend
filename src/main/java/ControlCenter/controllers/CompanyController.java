package ControlCenter.controllers;

import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.exception.*;
import ControlCenter.projection.TenantProjection;
import ControlCenter.repository.TenantRepository;
import ControlCenter.service.CompanyService;
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
@RequestMapping("/cc/company")
@RequiredArgsConstructor
public class CompanyController {
    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;
    private final TenantRepository tenantRepository;

    @GetMapping("/companyById")
    public ResponseEntity<CompanyDTO> getCompanyById(@RequestParam UUID internalId, @RequestParam LocalDate effectiveDate) {
        try {
            Optional<CompanyDTO> company = companyService.getCompanyByInternalId(internalId, effectiveDate);
            if (company.isEmpty()) {
                log.warn("No companies found by given id");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(company.get());
        } catch (Exception e) {
            log.error("Error fetching companies by given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companiesByTenant")
    public ResponseEntity<List<CompanyDTO>> getCompaniesByTenant(@RequestParam LocalDate effectiveDate) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            List<CompanyDTO> companies = companyService.getCompaniesByTenant(tenant.getInternalId(), effectiveDate);
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching companies by given tenant id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createCompany")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO request) {
        try {
            TenantProjection tenant = getTenantIdFromJWTToken();
            request.setTenant(tenant.getInternalId());
            return ResponseEntity.ok(companyService.createCompany(request));
        } catch (CompanyNotSavedException e) {
            log.info("Failed to create Company with external id {}", request.getExternalId());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/updateCompany/{companyId}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable UUID companyId,
                                                    @RequestParam LocalDate effectiveDate,
                                                    @RequestBody CompanyDTO request) {
        try {
            CompanyDTO updated = companyService.updateCompany(companyId, effectiveDate, request);
            log.info("Company with id {} is successfully updated", companyId);
            return ResponseEntity.ok(updated);
        } catch (CompanyNotFoundException e) {
            log.error("Failed to upsert Company: Company with id: {} could not be found", companyId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CompanyHistoryNotFoundException e) {
            log.error("Effective historical record for Company with id: {} and effective date: {} could not be found",
                    companyId, effectiveDate);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (CompanyWithImmutableUpdateException e) {
            log.error("Not allowed to update field(s) with name(s): {} for Company with id: {}",
                    e.getFieldNames(), companyId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createCompanyHistoricalRecord/{companyId}")
    public ResponseEntity<CompanyHistoryDTO> createCompanyHistoricalRecord(@PathVariable UUID companyId,
                                                                           @RequestBody CompanyHistoryDTO record) {
        try {
            CompanyHistoryDTO inserted = companyService.createCompanyHistoricalRecord(companyId, record);
            log.info("Company Historical Record for Company with id {} is successfully inserted", inserted.getCompanyId());
            return ResponseEntity.ok(inserted);
        } catch (CompanyNotFoundException e) {
            log.error("Failed to create historical record: Company with id: {} could not be found", companyId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CompanyHistoryFoundException e) {
            log.error("Failed to create historical record: History with start date: {} already exist", record.getStartDate());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
    }

    @DeleteMapping("/deleteCompanyHistoricalRecord/{companyId}")
    public ResponseEntity<CompanyDTO> deleteCompanyHistory(@PathVariable UUID companyId,
                                                           @RequestBody CompanyHistoryDTO record) {
        try {
            String logMessage = "Historical record with assigned to Company with id {} is deleted successfully";
            if(companyService.deleteCompanyHistoricalRecord(companyId, record)) {
                logMessage = "Company with with id {} and historical data is deleted successfully";
            }
            log.info(logMessage, companyId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CompanyNotFoundException e) {
            log.error("Failed to delete historical record: Company with id: {} could not be found", companyId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (CompanyHistoryNotFoundException e) {
            log.error("Failed to delete historical record: History with start date: {} could not be found", record.getStartDate());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<CompanyDTO> deleteCompany(@PathVariable UUID companyId) {
        try {
            companyService.deleteCompany(companyId);
            log.info("Company with id {} is deleted successfully", companyId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CompanyNotFoundException e) {
            log.error("Failed to delete Company: Company with id: {} could not be found", companyId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
