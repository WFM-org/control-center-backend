package wfm.tenant.ControlCenter.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import wfm.tenant.ControlCenter.entity.Tenant;

import java.util.UUID;

@JsonPropertyOrder({"id", "tenantId", "tenantName", "adminEmail", "localeDefault"})
public interface TenantProjection {
    UUID getId();

    String getTenantId();

    String getTenantName();

    String getadminEmail();

    LocaleProjection getLocaleDefault();

    static TenantProjection mapToTenantProjection(Tenant tenant) {
        return new TenantProjection() {
            @Override
            public UUID getId() {
                return tenant.getId();
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
            public LocaleProjection getLocaleDefault() {
                return new LocaleProjection() {
                    @Override
                    public String getLocaleId() {
                        return tenant.getLocaleDefault().getLocaleId();
                    }

                    @Override
                    public String getLocaleName() {
                        return tenant.getLocaleDefault().getLocaleName();
                    }
                };
            }
        };
    }
}


