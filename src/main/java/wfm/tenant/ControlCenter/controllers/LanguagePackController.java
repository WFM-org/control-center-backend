package wfm.tenant.ControlCenter.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wfm.tenant.ControlCenter.dto.AssignLanguagePackDTO;
import wfm.tenant.ControlCenter.entity.LanguagePack;
import wfm.tenant.ControlCenter.exception.LanguagePackNotFoundException;
import wfm.tenant.ControlCenter.exception.TenantNotFoundException;
import wfm.tenant.ControlCenter.projection.LanguagePackEnabledProjection;
import wfm.tenant.ControlCenter.projection.TenantProjection;
import wfm.tenant.ControlCenter.repository.TenantRepository;
import wfm.tenant.ControlCenter.service.LanguagePackService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cc/languagePack")
@RequiredArgsConstructor
@Slf4j
public class LanguagePackController {
    private final LanguagePackService languagePackService;
    private final TenantRepository tenantRepository;

    @GetMapping("/byTenantId")
    public ResponseEntity<List<LanguagePackEnabledProjection>> getLanguagePacksByTenant(@RequestParam("tenantId") UUID tenantId) {
        try {
            List<LanguagePackEnabledProjection> languagePacks = languagePackService.getLanguagePacksByTenantId(tenantId);
            return ResponseEntity.ok(languagePacks);
        } catch (Exception e) {
            log.error("Could not get language packs for tenant by given id", e);
            return ResponseEntity.noContent().build();
        }
    }

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

    @PostMapping("/assignLanguagePackTenant")
    public ResponseEntity<String> assignLanguagePackToTenant(@RequestBody AssignLanguagePackDTO request) {
        try {
            boolean success = languagePackService.addTenantLanguagePack(request.getId(), request.getLanguagePackId());
            if (success) {
                return ResponseEntity.ok("language pack " + request.getLanguagePackId() + " successfully assigned to Tenant " + request.getId());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("language pack " + request.getLanguagePackId() + " is already assigned to Tenant " + request.getId());
            }
        } catch (IllegalArgumentException e) {
            log.error("Error assigning language pack", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while assigning language pack", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/unassignLanguagePackTenant")
    public ResponseEntity<HttpStatus> unassignLanguagePackToTenant(@RequestParam("languagePackId") String languagePackId) {
        TenantProjection first = tenantRepository.findAllTenants().getFirst();
        try {
            languagePackService.unassignLanguagePack(languagePackId, first.getId());
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (TenantNotFoundException e) {
            log.error("Tenant with ID {} could not be found", e.getTenantId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (LanguagePackNotFoundException e) {
            log.error("Language Pack for combined Tenant with ID {} and Language Pack with ID {} could not be found",
                    e.getTenantId(), e.getLanguagePackId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
