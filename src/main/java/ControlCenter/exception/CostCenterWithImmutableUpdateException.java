package ControlCenter.exception;

import lombok.Getter;

@Getter
public class CostCenterWithImmutableUpdateException extends Throwable {
    private final String fieldNames;
    public CostCenterWithImmutableUpdateException(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
