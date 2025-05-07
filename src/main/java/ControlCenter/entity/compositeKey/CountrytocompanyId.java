package ControlCenter.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CountrytocompanyId implements Serializable {
    private static final long serialVersionUID = 5117148570508155459L;
    @Column(name = "country", nullable = false, length = 3)
    private String country;

    @Column(name = "company", nullable = false)
    private UUID company;
}