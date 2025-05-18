package ControlCenter.exception;

import lombok.Getter;

@Getter
public class EmploymentWithImmutableUpdateException extends Throwable {
    private final String fieldNames;
    public EmploymentWithImmutableUpdateException(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
