package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseStationForMap {

    @JsonProperty("name")
    public final String name;
    @JsonProperty("id")
    public final String id;
    @JsonProperty("lat")
    public final Double latitude;
    @JsonProperty("lon")
    public final Double longitude;

    public BaseStationForMap(String name, String id, Double latitude, Double longitude) {
        this.name = name;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
