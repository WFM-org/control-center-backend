package wfm.tenant.ControlCenter.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wfm.tenant.ControlCenter.entity.LanguagePackEnabled;
import wfm.tenant.ControlCenter.entity.LanguagePack;
import wfm.tenant.ControlCenter.entity.LanguagePackEnabledId;
import wfm.tenant.ControlCenter.entity.Tenant;
import wfm.tenant.ControlCenter.repository.LanguagePackEnabledRepository;
import wfm.tenant.ControlCenter.repository.LanguagePackRepository;
import wfm.tenant.ControlCenter.repository.TenantRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LanguagePackService {
    private final LanguagePackEnabledRepository languagePackEnabledRepository;
    private final LanguagePackRepository languagePackRepository;
    private final TenantRepository tenantRepository;

    public LanguagePackService(LanguagePackEnabledRepository languagePackEnabledRepository, LanguagePackRepository languagePackRepository, TenantRepository tenantRepository) {
        this.languagePackEnabledRepository = languagePackEnabledRepository;
        this.languagePackRepository = languagePackRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<String> getLanguagePacksByTenantId(UUID tenantId) {
        return languagePackEnabledRepository.findLanguagePacksByTenantId(tenantId);
    }

    public List<LanguagePack> getAllLanguagePacks() {
        return languagePackRepository.findAll();
    }

    @Transactional
    public boolean addTenantLanguagePack(UUID tenantInternalId, String languagePackId) {
        Tenant tenant = tenantRepository.findById(tenantInternalId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        LanguagePack languagePack = languagePackRepository.findById(languagePackId)
                .orElseThrow(() -> new IllegalArgumentException("Language Pack not found"));

        LanguagePackEnabledId languagePackEnabledId = new LanguagePackEnabledId(languagePack.getInternalId(), tenant.getId());

        //lad vær tilføj hvis den eksisterer
        if (languagePackEnabledRepository.existsById(languagePackEnabledId)) {
            log.warn("Language pack {} is already assigned to Tenant {}", languagePackId, tenantInternalId);
            return false;
        }

        LanguagePackEnabled languagePackEnabled = new LanguagePackEnabled();
        languagePackEnabled.setId(languagePackEnabledId);
        languagePackEnabled.setLanguagePack(languagePack);
        languagePackEnabled.setTenant(tenant);
        languagePackEnabledRepository.save(languagePackEnabled);

        log.info("Language pack {} assigned to Tenant {}", languagePackId, tenantInternalId);
        return true;
    }

    public List<String> getLanguagePacksByTenant(UUID tenantId) {
        return languagePackEnabledRepository.findLanguagePacksByTenantId(tenantId);
    }

}
