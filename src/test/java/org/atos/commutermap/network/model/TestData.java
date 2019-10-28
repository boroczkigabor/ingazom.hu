package org.atos.commutermap.network.model;

import org.atos.commutermap.model.Station;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TestData {

    public static TravelOfferRequest createTravelOfferRequest() {
        return defaultBuilder()
                .build();
    }

    static TravelOfferRequest.Builder defaultBuilder() {
        return TravelOfferRequest.builder()
                .withDeparture(new Station("005510009", "Maglod"))
                .withDepartureDateTime(LocalDateTime.ofEpochSecond(1571911800L, 0, ZoneOffset.UTC))
                .withDestination(new Station("005511551", "BUDAPEST*"))
                .withPassengers(new Passenger());
    }
}
