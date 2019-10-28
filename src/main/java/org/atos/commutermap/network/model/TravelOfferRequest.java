package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.atos.commutermap.model.Station;
import org.atos.commutermap.network.model.util.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public TravelOfferRequest(Builder builder) {
        this.departure = builder.departure;
        this.departureDateTime = builder.departureDateTime;
        this.destination = builder.destination;
        this.passengers = builder.passengers;
    }

    public static Builder builder () {
        return new Builder();
    }

    public static class Builder {

        private Station departure;
        private LocalDateTime departureDateTime;
        private Station destination;
        private final List<Passenger> passengers = new ArrayList<>();

        public Builder withDeparture(Station departure) {
            this.departure = departure;
            return this;
        }

        public Builder withDepartureDateTime(LocalDateTime departureDateTime) {
            this.departureDateTime = departureDateTime;
            return this;
        }

        public Builder withDestination(Station destination) {
            this.destination = destination;
            return this;
        }

        public Builder withPassengers(Passenger passenger) {
            this.passengers.add(passenger);
            return this;
        }

        public TravelOfferRequest build() {
            return new TravelOfferRequest(this);
        }
    }
}
