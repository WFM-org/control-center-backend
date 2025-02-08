package wfm.tenant.ControlCenter.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wfm.tenant.ControlCenter.projection.CompanyProjection;
import wfm.tenant.ControlCenter.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<CompanyProjection>> getAllCompanies() {
        try {
            List<CompanyProjection> companies = companyService.getAllCompanyProjection();
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

}
