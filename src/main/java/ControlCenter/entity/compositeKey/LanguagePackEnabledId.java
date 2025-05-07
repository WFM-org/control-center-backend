package ControlCenter.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LanguagePackEnabledId implements Serializable {

    private static final long serialVersionUID = -1214776407967732525L;

    @Column(name = "language_pack", nullable = false, length = 10)
    private String languagePack;

    @Column(name = "tenant", nullable = false)
    private UUID tenant;
}