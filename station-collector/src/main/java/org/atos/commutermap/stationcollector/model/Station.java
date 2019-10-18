package org.atos.commutermap.stationcollector.model;

public class Station {

    public final String name;
    public final Coordinates coordinates;

    public Station(String name, Coordinates coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }
}
