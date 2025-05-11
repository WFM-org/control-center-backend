package ControlCenter.controllers;

import ControlCenter.dto.CompanyDTO;
import ControlCenter.projection.TenantProjection;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//    @GetMapping("/getAllCompanies")
//    public ResponseEntity<List<CompanyProjection>> getAllCompanies() {
//        try {
//            List<CompanyProjection> companies = companyService.getAllCompanies();
//            if (companies.isEmpty()) {
//                log.warn("No companies found");
//                return ResponseEntity.noContent().build();
//            }
//            return ResponseEntity.ok(companies);
//        } catch (Exception e) {
//            log.error("Error fetching companies", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

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
            List<CompanyDTO> companies = companyService.getCompaniesByTenant(tenant.getId(), effectiveDate);
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching companies by given tenant id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

//    @PostMapping("/createCompany")
//    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company request) {
//        try {
//            TenantProjection first = tenantRepository.findAllTenants().getFirst();
//            Company company = companyService.createCompany(request.getExternalId(),
//                    request.getName(), first.getId());
//            return ResponseEntity.ok(company);
//        } catch (TenantNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

//    @DeleteMapping("/deleteCompany/{companyId}")
//    public ResponseEntity<CompanyProjection> deleteCompany(@PathVariable UUID companyId) {
//        try {
//            CompanyProjection deleted = companyService.deleteCompanyById(companyId);
//            log.info("Company with id {} is deleted successfully", companyId);
//            return ResponseEntity.ok(deleted);
//        } catch (CompanyNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

//    @PostMapping("/updateCompany/{companyId}")
//    public ResponseEntity<CompanyProjection> updateCompany(@PathVariable UUID companyId, @RequestBody Company request) {
//        try {
//            CompanyProjection updated = companyService.updateCompany(request, companyId);
//            log.info("Company with id {} is updated successfully", companyId);
//            return ResponseEntity.ok(updated);
//        } catch (CompanyNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (ImmutableUpdateException e) {
//            log.error("Not allowed to update field(s) with name(s): {}", e.getFieldNames());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
}
