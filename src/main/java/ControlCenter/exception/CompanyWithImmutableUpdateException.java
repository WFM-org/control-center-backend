package ControlCenter.exception;

import lombok.Getter;

@Getter
public class CompanyWithImmutableUpdateException extends Throwable {
    private final String fieldNames;
    public CompanyWithImmutableUpdateException(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
