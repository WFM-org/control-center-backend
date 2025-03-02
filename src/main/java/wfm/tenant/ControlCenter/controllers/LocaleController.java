package wfm.tenant.ControlCenter.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wfm.tenant.ControlCenter.dto.AssignLocaleDTO;
import wfm.tenant.ControlCenter.entity.Locale;
import wfm.tenant.ControlCenter.projection.TenantProjection;
import wfm.tenant.ControlCenter.service.LocaleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locale")
@RequiredArgsConstructor
@Slf4j
public class LocaleController {
    private final LocaleService localeService;

    @GetMapping("/byTenantId")
    public ResponseEntity<List<String>> getLocalesByTenant(@RequestParam("tenantId") UUID tenantId) {
        try {
            List<String> locales = localeService.getLocalesByTenantId(tenantId);
            return ResponseEntity.ok(locales);
        } catch (Exception e) {
            log.error("Could not get locales for tenant by given id", e);
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/allLocales")
    public ResponseEntity<List<Locale>> getAllLocales() {
        try {
            List<Locale> allLocales = localeService.getAllLocales();
            return ResponseEntity.ok(allLocales);

        } catch (Exception e) {
            log.error("Could not get locales", e);
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/assignLocaleTenant")
    public ResponseEntity<String> assignLocaleToTenant(@RequestBody AssignLocaleDTO request) {
        try {
            boolean success = localeService.addTenantLocale(request.getId(), request.getLocaleId());
            if (success) {
                return ResponseEntity.ok("Locale " + request.getLocaleId() + " successfully assigned to Tenant " + request.getId());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Locale " + request.getLocaleId() + " is already assigned to Tenant " + request.getId());
            }
        } catch (IllegalArgumentException e) {
            log.error("Error assigning locale", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while assigning locale", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
