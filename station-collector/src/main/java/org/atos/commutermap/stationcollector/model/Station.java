package org.atos.commutermap.stationcollector.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Station {

    public final String id;
    public final String name;

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
