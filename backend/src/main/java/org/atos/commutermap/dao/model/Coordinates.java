package org.atos.commutermap.dao.model;

import org.atos.commutermap.dao.util.CoordinateToDoubleConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Embeddable
public class Coordinates extends BaseClass {

    @Convert(converter = CoordinateToDoubleConverter.class)
    @Column(name = "lat")
    public final Double latitude;
    @Convert(converter = CoordinateToDoubleConverter.class)
    @Column(name = "lon")
    public final Double longitude;

    private Coordinates() {
        this.latitude = null;
        this.longitude = null;
    }

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
