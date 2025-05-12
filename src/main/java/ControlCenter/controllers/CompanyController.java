package ControlCenter.controllers;

import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.exception.CompanyControlUnknownError;
import ControlCenter.exception.CompanyNotFoundException;
import ControlCenter.exception.ImmutableUpdateException;
import ControlCenter.exception.TenantNotFoundException;
import ControlCenter.projection.TenantProjection;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ControlCenter.repository.TenantRepository;
import ControlCenter.service.CompanyService;

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
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable UUID internalId, @RequestBody CompanyDTO request) {
        try {
            CompanyDTO updated = companyService.updateCompany(internalId, request);
            log.info("Company with id {} is successfully updated", internalId);
            return ResponseEntity.ok(updated);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ImmutableUpdateException e) {
            log.error("Not allowed to update field(s) with name(s): {}", e.getFieldNames());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createCompanyHistoricalRecord/{companyId}")
    public ResponseEntity<CompanyHistoryDTO> updateCompany(@PathVariable UUID internalId, @RequestBody CompanyHistoryDTO request) {
        try {
            CompanyHistoryDTO updated = companyService.createCompanyHistoricalRecord(internalId, request);
            log.info("Company Historical Record for Company with id {} is successfully inserted", updated.getCompanyId());
            return ResponseEntity.ok(updated);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ImmutableUpdateException e) {
            log.error("Not allowed to update field(s) with name(s): {}", e.getFieldNames());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<CompanyDTO> deleteCompany(@PathVariable UUID internalId) {
        try {
            companyService.deleteCompany(internalId);
            log.info("Company with id {} is deleted successfully", internalId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }

//    @GetMapping("/companyByName")
//    public ResponseEntity<List<CompanyProjection>> getCompanyByName(@RequestParam String companyName) {
//        try {
//            List<CompanyProjection> companiesByName = companyService.getCompanyByName(companyName);
//            if (companiesByName.isEmpty()) {
//                log.warn("No companies found by given name");
//                return ResponseEntity.noContent().build();
//            }
//            return ResponseEntity.ok(companiesByName);
//        } catch (Exception e) {
//            log.error("Error fetching companies by given name", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


}
