package ControlCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language_pack")
@AllArgsConstructor
@NoArgsConstructor
public class LanguagePack {
    @Id
    @Column(name = "internal_id", nullable = false, length = 10)
    private String internalId;

    @Column(name = "name", nullable = false, length = 32)
    private String languageName;

}