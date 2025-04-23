package wfm.tenant.ControlCenter.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wfm.tenant.ControlCenter.entity.LanguagePack;
import wfm.tenant.ControlCenter.exception.LanguagePackAlreadyAssignedException;
import wfm.tenant.ControlCenter.exception.LanguagePackNotFoundException;
import wfm.tenant.ControlCenter.exception.TenantNotFoundException;
import wfm.tenant.ControlCenter.projection.LanguagePackEnabledProjection;
import wfm.tenant.ControlCenter.projection.TenantProjection;
import wfm.tenant.ControlCenter.repository.TenantRepository;
import wfm.tenant.ControlCenter.service.LanguagePackService;

import java.util.List;

@RestController
@RequestMapping("/cc/languagePack")
@RequiredArgsConstructor
@Slf4j
public class LanguagePackController {
    private final LanguagePackService languagePackService;
    private final TenantRepository tenantRepository;

    @GetMapping("/allLanguagePacks")
    public ResponseEntity<List<LanguagePack>> getAllLanguagePacks() {
        try {
            List<LanguagePack> allLanguagePacks = languagePackService.getAllLanguagePacks();
            return ResponseEntity.ok(allLanguagePacks);

        } catch (Exception e) {
            log.error("Could not get language packs", e);
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/byTenant")
    public ResponseEntity<List<LanguagePackEnabledProjection>> getLanguagePacksByTenant() {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            List<LanguagePackEnabledProjection> languagePacks = languagePackService.getLanguagePacksByTenantId(tenant.getId());
            return ResponseEntity.ok(languagePacks);
        } catch (Exception e) {
            log.error("Could not get language packs for tenant by given id", e);
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/assignLanguagePackTenant")
    public ResponseEntity<HttpStatus> assignLanguagePackToTenant(@RequestParam("languagePackId") String languagePackId) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            languagePackService.assignLanguagePack(tenant.getId(), languagePackId);
            log.info("Language Pack with ID {} is assigned to Tenant with ID {}", languagePackId, tenant.getId());
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (LanguagePackNotFoundException e) {
            log.error("Language pack {} is already assigned to Tenant {}", e.getLanguagePackId(), e.getTenantId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (TenantNotFoundException e) {
            log.error("Tenant with ID {} could not be found. Language Pack with ID {} was not assigned.",
                    e.getTenantId(), languagePackId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (LanguagePackAlreadyAssignedException e) {
            log.warn("Language Pack for combined Tenant with ID {} and Language Pack with ID {} do already exist",
                    e.getTenantId(), e.getLanguagePackId());
            return ResponseEntity.ok(HttpStatus.ALREADY_REPORTED);

        }
    }

    @PostMapping("/unassignLanguagePackTenant")
    public ResponseEntity<HttpStatus> unassignLanguagePackToTenant(@RequestParam("languagePackId") String languagePackId) {
        TenantProjection tenant = getTenantIdFromJWTToken();
        try {
            languagePackService.unassignLanguagePack(languagePackId, tenant.getId());
            log.warn("Language Pack with ID {} is removed from Tenant with ID {}", languagePackId, tenant.getId());
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (TenantNotFoundException e) {
            log.error("Tenant with ID {} could not be found. Language Pack with ID {} was not removed.",
                    e.getTenantId(), languagePackId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (LanguagePackNotFoundException e) {
            log.error("Language Pack for combined Tenant with ID {} and Language Pack with ID {} could not be found",
                    e.getTenantId(), e.getLanguagePackId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // TODO: Real implementation
    private TenantProjection getTenantIdFromJWTToken() {
        return tenantRepository.findAllTenants().getFirst();
    }
}
