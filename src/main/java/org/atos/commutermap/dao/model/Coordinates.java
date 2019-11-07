package org.atos.commutermap.dao.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Coordinates {

    @Column(name = "lat")
    public final Long latitude;
    @Column(name = "lon")
    public final Long longitude;

    private Coordinates() {
        this.latitude = null;
        this.longitude = null;
    }

    public Coordinates(Long latitude, Long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
