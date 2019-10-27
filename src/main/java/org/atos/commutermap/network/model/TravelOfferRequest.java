package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.atos.commutermap.model.Station;
import org.atos.commutermap.network.model.util.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.atos.commutermap.network.model.FieldNames.*;

@JsonPropertyOrder(alphabetic = true)
public class TravelOfferRequest {
    @JsonProperty(WITHOUT_CACHE)
    public final boolean withoutCache = false;
    @JsonProperty(DEPARTURE_STATION)
    public final Station departure;
    @JsonProperty(DATE)
    @JsonSerialize (using = LocalDateTimeSerializer.class)
    public final LocalDateTime departureDateTime;
    @JsonProperty(DESTINATION_STATION)
    public final Station destination;
    @JsonProperty(FieldNames.IS_DEPARTURE_TIME)
    public final boolean isDepartureDateTime = true;
    @JsonProperty(SEARCH_CONDITIONS)
    public final List<String> searchConditions = Collections.emptyList();
    @JsonProperty(LANGUAGE)
    public final String language = "HU";
    @JsonProperty(SERVICES)
    public final List<String> services = Collections.singletonList("52");
    @JsonProperty(UAID)
    public final String userIdentifier = "0oX97K2A9ONK1RaMke2Fee62202b";
    @JsonProperty(PASSENGERS)
    public final List<Passenger> passengers;

    public TravelOfferRequest(Station departure, LocalDateTime departureDateTime, Station destination, List<Passenger> passengers) {
        this.departure = departure;
        this.departureDateTime = departureDateTime;
        this.destination = destination;
        this.passengers = passengers;
    }
}
