package ControlCenter.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ControlCenter.entity.Tenant;

import java.util.UUID;

@JsonPropertyOrder({"internalId", "tenantId", "tenantName", "adminEmail", "languagePackDefault"})
public interface TenantProjection {
    UUID getInternalId();

    String getTenantId();

    String getTenantName();

    String getadminEmail();

    LanguagePackProjection getLanguagePackDefault();

    static TenantProjection mapToTenantProjection(Tenant tenant) {
        return new TenantProjection() {
            @Override
            public UUID getInternalId() {
                return tenant.getInternalId();
            }

            @Override
            public String getTenantId() {
                return tenant.getTenantId();
            }

            @Override
            public String getTenantName() {
                return tenant.getTenantName();
            }

            @Override
            public String getadminEmail() {
                return tenant.getAdminEmail();
            }

            @Override
            public LanguagePackProjection getLanguagePackDefault() {
                return new LanguagePackProjection() {
                    @Override
                    public String getInternalId() {
                        return tenant.getLanguagePackDefault().getInternalId();                    }

                    @Override
                    public String getLanguageName() {
                        return tenant.getLanguagePackDefault().getLanguageName();
                    }
                };
            }
        };
    }
}


