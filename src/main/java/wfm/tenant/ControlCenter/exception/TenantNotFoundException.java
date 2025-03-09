package wfm.tenant.ControlCenter.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TenantNotFoundException extends Throwable {
    private UUID tenantId;

    public TenantNotFoundException(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
