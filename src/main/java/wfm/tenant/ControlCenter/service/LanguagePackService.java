package wfm.tenant.ControlCenter.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wfm.tenant.ControlCenter.entity.LanguagePack;
import wfm.tenant.ControlCenter.entity.LanguagePackEnabled;
import wfm.tenant.ControlCenter.entity.LanguagePackEnabledId;
import wfm.tenant.ControlCenter.entity.Tenant;
import wfm.tenant.ControlCenter.exception.LanguagePackAlreadyAssignedException;
import wfm.tenant.ControlCenter.exception.LanguagePackNotFoundException;
import wfm.tenant.ControlCenter.exception.TenantNotFoundException;
import wfm.tenant.ControlCenter.projection.LanguagePackEnabledProjection;
import wfm.tenant.ControlCenter.repository.LanguagePackEnabledRepository;
import wfm.tenant.ControlCenter.repository.LanguagePackRepository;
import wfm.tenant.ControlCenter.repository.TenantRepository;

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
        if(tenant.isEmpty()) {
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
