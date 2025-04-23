package ControlCenter.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({"id", "tenant", "externalId", "name", "languagePackDefault", "recordStatus"})
public interface CompanyProjection {

    UUID getId();

    UUID getTenant();

    String getExternalId();

    String getName();

    LanguagePackProjection getlanguagePackDefault();

    Short getRecordStatus();
}
