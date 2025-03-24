package wfm.tenant.ControlCenter.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LanguagePackAlreadyAssignedException extends Throwable {
    private final String languagePackId;
    private final UUID tenantId;
    public LanguagePackAlreadyAssignedException(String languagePackId, UUID tenantId) {
        this.languagePackId = languagePackId;
        this.tenantId = tenantId;
    }
}
