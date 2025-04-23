package ControlCenter.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TenantNotFoundException extends Throwable {
    private final UUID tenantId;

    public TenantNotFoundException(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
