package ControlCenter.exception;

import lombok.Getter;

@Getter
public class PersonWithImmutableUpdateException extends Throwable {
    private final String fieldNames;
    public PersonWithImmutableUpdateException(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
