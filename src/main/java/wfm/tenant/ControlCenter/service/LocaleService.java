package wfm.tenant.ControlCenter.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wfm.tenant.ControlCenter.entity.Localeenabled;
import wfm.tenant.ControlCenter.entity.Locale;
import wfm.tenant.ControlCenter.entity.LocaleenabledId;
import wfm.tenant.ControlCenter.entity.Tenant;
import wfm.tenant.ControlCenter.repository.LocaleEnabledRepository;
import wfm.tenant.ControlCenter.repository.LocaleRepository;
import wfm.tenant.ControlCenter.repository.TenantRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LocaleService {
    private final LocaleEnabledRepository localeEnabledRepository;
    private final LocaleRepository localeRepository;
    private final TenantRepository tenantRepository;

    public LocaleService(LocaleEnabledRepository localeEnabledRepository, LocaleRepository localeRepository, TenantRepository tenantRepository) {
        this.localeEnabledRepository = localeEnabledRepository;
        this.localeRepository = localeRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<String> getLocalesByTenantId(UUID tenantId) {
        return localeEnabledRepository.findLocalesByTenantId(tenantId);
    }

    public List<Locale> getAllLocales() {
        return localeRepository.findAll();
    }

    @Transactional
    public boolean addTenantLocale(UUID tenantInternalId, String localeId) {
        Tenant tenant = tenantRepository.findById(tenantInternalId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        Locale locale = localeRepository.findById(localeId)
                .orElseThrow(() -> new IllegalArgumentException("Locale not found"));

        LocaleenabledId localeenabledId = new LocaleenabledId(locale.getLocaleId(), tenant.getId());

        //lad vær tilføj hvis den eksisterer
        if (localeEnabledRepository.existsById(localeenabledId)) {
            log.warn("Locale {} is already assigned to Tenant {}", localeId, tenantInternalId);
            return false;
        }

        Localeenabled localeenabled = new Localeenabled();
        localeenabled.setId(localeenabledId);
        localeenabled.setLocale(locale);
        localeenabled.setTenant(tenant);
        localeEnabledRepository.save(localeenabled);

        log.info("Locale {} assigned to Tenant {}", localeId, tenantInternalId);
        return true;
    }

    public List<String> getLocalesByTenant(UUID tenantId) {
        return localeEnabledRepository.findLocalesByTenantId(tenantId);
    }

}
