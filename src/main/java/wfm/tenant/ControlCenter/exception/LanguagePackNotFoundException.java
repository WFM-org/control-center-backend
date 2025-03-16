package wfm.tenant.ControlCenter.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LanguagePackNotFoundException extends Throwable {
    String languagePackId;
    UUID tenantId;
    public LanguagePackNotFoundException(String languagePackId, UUID tenantId) {
        this.languagePackId = languagePackId;
        this.tenantId = tenantId;
    }
}
