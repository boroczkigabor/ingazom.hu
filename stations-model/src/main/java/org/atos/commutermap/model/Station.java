package org.atos.commutermap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Station {

    @JsonValue
    public final String id;
    @JsonIgnore
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
