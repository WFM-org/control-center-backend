package ControlCenter.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ControlCenter.entity.LanguagePack;
import ControlCenter.entity.LanguagePackEnabled;
import ControlCenter.entity.LanguagePackEnabledId;
import ControlCenter.entity.Tenant;
import ControlCenter.exception.LanguagePackAlreadyAssignedException;
import ControlCenter.exception.LanguagePackNotFoundException;
import ControlCenter.exception.TenantNotFoundException;
import ControlCenter.projection.LanguagePackEnabledProjection;
import ControlCenter.repository.LanguagePackEnabledRepository;
import ControlCenter.repository.LanguagePackRepository;
import ControlCenter.repository.TenantRepository;

import java.util.List;
import java.util.Optional;
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

    public List<LanguagePackEnabledProjection> getLanguagePacksByTenantId(UUID tenantId) {
        return languagePackEnabledRepository.findLanguagePacksByTenantId(tenantId);
    }

    public List<LanguagePack> getAllLanguagePacks() {
        return languagePackRepository.findAll();
    }

    @Transactional
    public void assignLanguagePack(UUID tenantId, String languagePackId) throws TenantNotFoundException, LanguagePackAlreadyAssignedException, LanguagePackNotFoundException {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        LanguagePack languagePack = languagePackRepository.findById(languagePackId)
                .orElseThrow(() -> new LanguagePackNotFoundException(languagePackId, tenantId));

        Optional<LanguagePackEnabled> existId = languagePackEnabledRepository.findById(new LanguagePackEnabledId(languagePackId, tenant.getId()));
        if (existId.isPresent()) {
            throw new LanguagePackAlreadyAssignedException(languagePackId, tenantId);
        }

        LanguagePackEnabled languagePackEnabled = new LanguagePackEnabled();
        languagePackEnabled.setId(new LanguagePackEnabledId(languagePack.getInternalId(), tenant.getId()));
        languagePackEnabled.setLanguagePack(languagePack);
        languagePackEnabled.setTenant(tenant);
        languagePackEnabledRepository.save(languagePackEnabled);
    }

    public LanguagePack getDefaultLanguagePackByTenantId(UUID tenantId) throws TenantNotFoundException {
        Optional<Tenant> tenant = tenantRepository.findById(tenantId);
        if (tenant.isEmpty()) {
            log.error("Tenant with ID {} could not be found", tenantId);
            throw new TenantNotFoundException(tenantId);
        }
        return tenant.get().getLanguagePackDefault();
    }

    @Transactional
    public void unassignLanguagePack(String languagePackId, UUID tenantId) throws TenantNotFoundException, LanguagePackNotFoundException {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));
        LanguagePackEnabled byId = languagePackEnabledRepository.findById(new LanguagePackEnabledId(languagePackId, tenant.getId()))
                .orElseThrow(() -> new LanguagePackNotFoundException(languagePackId, tenantId));
        languagePackEnabledRepository.delete(byId);
    }
}
