package org.atos.commutermap.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "station")
public class Station implements Serializable {

    @JsonValue
    @Id
    public final String id;
    @JsonIgnore
    public final String name;

    protected Station() {
        this.id = null;
        this.name = null;
    }

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
