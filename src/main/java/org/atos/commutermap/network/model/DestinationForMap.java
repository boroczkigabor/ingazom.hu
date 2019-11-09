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
    @JsonProperty("elviraUrl")
    public final String elviraUrl;

    public DestinationForMap(String destinationName, Double latitude, Double longitude, Long minutesOfTravel, String color, String elviraUrl) {
        this.destinationName = destinationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minutesOfTravel = String.valueOf(minutesOfTravel);
        this.color = color;
        this.elviraUrl = elviraUrl;
    }

}
