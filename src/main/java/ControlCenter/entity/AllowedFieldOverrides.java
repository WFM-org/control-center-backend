package ControlCenter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "allowed_field_overrides")
public class AllowedFieldOverrides {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "field_name", nullable = false)
    private String fieldName;
}
