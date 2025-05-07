package ControlCenter.projection;

import ControlCenter.entity.compositeKey.CompanyHistoryId;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;

@JsonPropertyOrder({"id", "endDate", "name", "timezone", "recordStatus", "languagePackDefault"})
public interface CompanyHistoryProjection {

    CompanyHistoryId getId();

    LocalDate getEndDate();

    String getName();

    String getTimezone();

    Short getRecordStatus();

    LanguagePackProjection getLanguagePackDefault();

}
