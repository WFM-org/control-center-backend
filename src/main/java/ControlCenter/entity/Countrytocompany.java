package ControlCenter.entity;

import ControlCenter.entity.compositeKey.CountrytocompanyId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "country_to_company")
public class Countrytocompany {
    @EmbeddedId
    private CountrytocompanyId id;

    @MapsId("country")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    @MapsId("company")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company", nullable = false)
    private Company company;

}