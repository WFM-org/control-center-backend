package ControlCenter.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LanguagePackNotFoundException extends Throwable {
    private final String languagePackId;
    private final UUID tenantId;

    public LanguagePackNotFoundException(String languagePackId, UUID tenantId) {
        this.languagePackId = languagePackId;
        this.tenantId = tenantId;
    }
}
