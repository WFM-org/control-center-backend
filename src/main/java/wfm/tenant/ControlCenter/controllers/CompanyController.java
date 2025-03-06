package wfm.tenant.ControlCenter.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wfm.tenant.ControlCenter.entity.Company;
import wfm.tenant.ControlCenter.projection.CompanyProjection;
import wfm.tenant.ControlCenter.exception.CompanyNotFoundException;
import wfm.tenant.ControlCenter.service.CompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;

    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<CompanyProjection>> getAllCompanies() {
        try {
            List<CompanyProjection> companies = companyService.getAllCompanies();
            if (companies.isEmpty()) {
                log.warn("No companies found");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching companies", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companyById")
    public ResponseEntity<List<CompanyProjection>> getCompanyById(@RequestParam String id) {
        try {
            List<CompanyProjection> companiesById = companyService.getCompanyByExternalId(id);
            if (companiesById.isEmpty()) {
                log.warn("No companies found by given id");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(companiesById);
        } catch (Exception e) {
            log.error("Error fetching companies by given id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companyByName")
    public ResponseEntity<List<CompanyProjection>> getCompanyByName(@RequestParam String companyName) {
        try {
            List<CompanyProjection> companiesByName = companyService.getCompanyByName(companyName);
            if (companiesByName.isEmpty()) {
                log.warn("No companies found by given name");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(companiesByName);
        } catch (Exception e) {
            log.error("Error fetching companies by given name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createCompany")
    public ResponseEntity<String> createCompany(@Valid @RequestBody Company request) {
        try {
            companyService.createCompany(request.getExternalId(), request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body("Company created succesfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating company: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<CompanyProjection> deleteCompany(@PathVariable UUID companyId) {
        try {
            CompanyProjection deleted = companyService.deleteCompanyById(companyId);
            log.info("Company with id {} is deleted successfully", companyId);
            return ResponseEntity.ok(deleted);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/updateCompany/{companyId}")
    public ResponseEntity<CompanyProjection> updateCompany(@PathVariable UUID companyId, @RequestBody Company request) {
        try {
            CompanyProjection updated = companyService.updateCompany(request, companyId);
            log.info("Company with id {} is updated successfully", companyId);
            return ResponseEntity.ok(updated);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
