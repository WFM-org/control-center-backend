package wfm.tenant.ControlCenter.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wfm.tenant.ControlCenter.dto.AssignLanguagePackDTO;
import wfm.tenant.ControlCenter.entity.LanguagePack;
import wfm.tenant.ControlCenter.service.LanguagePackService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cc/languagePack")
@RequiredArgsConstructor
@Slf4j
public class LanguagePackController {
    private final LanguagePackService languagePackService;

    @GetMapping("/byTenantId")
    public ResponseEntity<List<String>> getLanguagePacksByTenant(@RequestParam("tenantId") UUID tenantId) {
        try {
            List<String> languagePacks = languagePackService.getLanguagePacksByTenantId(tenantId);
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
}
