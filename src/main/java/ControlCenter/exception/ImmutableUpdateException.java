package ControlCenter.exception;

import lombok.Getter;

@Getter
public class ImmutableUpdateException extends Throwable {
    private final String fieldNames;

    public ImmutableUpdateException(String fieldNames) {
        this.fieldNames = fieldNames;
    }
}
