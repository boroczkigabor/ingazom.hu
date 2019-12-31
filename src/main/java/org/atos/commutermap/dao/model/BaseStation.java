package org.atos.commutermap.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "BaseStation")
@Table(name = "baseStation")
public class BaseStation extends Station {

    public final double maxDistance;
    public final int maxDuration;

    protected BaseStation() {
        super();
        this.maxDistance = Double.NaN;
        this.maxDuration = Integer.MIN_VALUE;
    }

    public BaseStation(String id, String name, Coordinates coordinates, double maxDistance, int maxDuration) {
        super(id, name, coordinates);
        this.maxDistance = maxDistance;
        this.maxDuration = maxDuration;
    }
}
