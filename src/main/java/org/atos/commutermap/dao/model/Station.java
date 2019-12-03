package org.atos.commutermap.dao.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;
import javax.persistence.*;

@Entity
@Table(name = "station")
public class Station extends BaseClass {

    @JsonValue
    @Id
    public final String id;
    @Nonnull
    @JsonIgnore
    public final String name;
    @Nonnull
    @JsonIgnore
    @AttributeOverrides({
            @AttributeOverride( name = "lon", column = @Column(name = "lon")),
            @AttributeOverride( name = "lat", column = @Column(name = "lat"))
    })
    public final Coordinates coordinates;

    protected Station() {
        this.id = null;
        this.name = null;
        this.coordinates = null;
    }

    @JsonCreator
    private Station(String id) {
        this.id = id;
        this.name = null;
        this.coordinates = null;
    }

    public Station(String id, String name, Coordinates coordinates) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
    }

}
