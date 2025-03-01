package wfm.tenant.ControlCenter.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({"id", "tenant", "name", "externalid", "localeDefault"})
public interface CompanyProjection {

    UUID getId();

    UUID getTenant();

    String getExternalId();

    String getName();

    LocaleProjection getLocaledefault();

    Short getRecordStatus();
}
