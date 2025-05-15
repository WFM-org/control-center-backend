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
            return ResponseEntity.ok(companyService.createCompany(request));
        } catch (TenantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CompanyControlUnknownError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            log.error("Failed to update: Company with id: {} could not be found", companyId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CompanyHistoryNotFoundException e) {
            log.error("Effective historical record for Company with id: {} and effective date: {} could not be found",
                    companyId, effectiveDate);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ImmutableUpdateException e) {
            log.error("Not allowed to update field(s) with name(s): {}", e.getFieldNames());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createCompanyHistoricalRecord/{companyId}")
    public ResponseEntity<CompanyHistoryDTO> createCompanyHistoricalRecord(@PathVariable UUID companyId,
                                                                           @RequestBody CompanyHistoryDTO request) {
        try {
            CompanyHistoryDTO inserted = companyService.createCompanyHistoricalRecord(companyId, request);
            log.info("Company Historical Record for Company with id {} is successfully inserted", inserted.getCompanyId());
            return ResponseEntity.ok(inserted);
        } catch (CompanyNotFoundException e) {
            log.error("Failed to create historical record: Company with id: {} could not be found", companyId);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/deleteCompanyHistory/{companyId}")
    public ResponseEntity<CompanyDTO> deleteCompanyHistory(@PathVariable UUID companyId,
                                                    @RequestBody CompanyHistoryDTO toDelete) {
        try {
            companyService.deleteCompanyHistoricalRecord(companyId, toDelete);
            log.info("Company with id {} is deleted successfully", companyId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (CompanyHistoryNotFoundException e) {
            log.error("Effective historical record for Company with id: {} and start date: {} could not be found",
                    companyId, toDelete.getStartDate());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
