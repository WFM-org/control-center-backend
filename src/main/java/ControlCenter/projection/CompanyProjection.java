package ControlCenter.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.UUID;

@JsonPropertyOrder({"internalId", "tenant", "externalId", "companyHistories"})
public interface CompanyProjection {

    UUID getInternalId();

    UUID getTenant();

    String getExternalId();

    List<CompanyHistoryProjection> getCompanyHistories();

    String getTimezone();

    LanguagePackProjection getLanguagePackDefault();
}
