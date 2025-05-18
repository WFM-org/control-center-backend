package ControlCenter.exception;

import lombok.Getter;

@Getter
public class OrgUnitWithImmutableUpdateException extends Exception {
    private final String fieldNames;

    public OrgUnitWithImmutableUpdateException(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
