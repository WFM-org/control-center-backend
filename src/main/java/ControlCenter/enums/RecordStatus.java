package ControlCenter.enums;

import lombok.Getter;

@Getter
public enum RecordStatus {
    INACTIVE((short) 0),
    ACTIVE((short) 1);

    private final short value;

    RecordStatus(final short i) {
        value = i;
    }
}
