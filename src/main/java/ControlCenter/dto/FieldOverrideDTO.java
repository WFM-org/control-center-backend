package ControlCenter.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FieldOverrideDTO {
    private String tableName;
    private String fieldName;
    private boolean visible;
    private boolean mandatory;
    private boolean editable;
}