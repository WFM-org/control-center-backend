package wfm.tenant.ControlCenter.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({"id", "tenantId", "companyName", "companyExternalId", "localeDefault"})
public interface CompanyProjection {

    Long getId();

    UUID getTenantId();

    String getCompanyName();

    String getCompanyExternalId();

    LocaleProjection getLocaleDefault();
}
