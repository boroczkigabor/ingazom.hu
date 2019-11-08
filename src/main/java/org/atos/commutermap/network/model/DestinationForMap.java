package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DestinationForMap implements Serializable {
    @JsonProperty("name")
    public final String destinationName;
    @JsonProperty("lat")
    public final Double latitude;
    @JsonProperty("lon")
    public final Double longitude;
    @JsonProperty("minutes")
    public final String minutesOfTravel;
    @JsonProperty("color")
    public final String color;

    public DestinationForMap(String destinationName, Double latitude, Double longitude, Long minutesOfTravel) {
        this.destinationName = destinationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minutesOfTravel = String.valueOf(minutesOfTravel);
        if (minutesOfTravel <= 20) {
            color = "green";
        } else if (minutesOfTravel <= 30) {
            color = "blue";
        } else if (minutesOfTravel <= 45) {
            color = "yellow";
        } else {
            color = "red";
        }
    }

}
