package ControlCenter.controllers;

import ControlCenter.dto.FieldOverrideDTO;
import ControlCenter.service.FieldOverrideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cc/field-overrides")
@RequiredArgsConstructor
public class FieldOverrideController {
    private final FieldOverrideService fieldOverrideService;

    //hemter allowed fields for tenantt med angivet id
    @GetMapping("/overridesByTenantId")
    public List<FieldOverrideDTO> getOverrides(@RequestParam UUID tenantId) {
        return fieldOverrideService.getOverridesForTenant(tenantId);
    }

    //tager en dto i body for at update via angivet tenant id
    @PutMapping
    public void updateOverride(@RequestParam UUID tenantId, @RequestBody FieldOverrideDTO request) {
        fieldOverrideService.updateOverride(tenantId, request);
    }
}

