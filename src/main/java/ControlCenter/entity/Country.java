package ControlCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "country")
public class Country {
    @Id
    @Column(name = "isocode3", nullable = false, length = 3)
    private String isocode3;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "isocode2", length = 2)
    private String isocode2;

}