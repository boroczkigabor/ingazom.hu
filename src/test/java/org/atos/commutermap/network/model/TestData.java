package org.atos.commutermap.network.model;

import org.atos.commutermap.dao.model.Coordinates;
import org.atos.commutermap.dao.model.Station;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TestData {

    public static final Station STATION_BAG = new Station("005511155", "Bag", new Coordinates(47.64100555, 19.47165867));
    public static final Station STATION_BUDAPEST_STAR = new Station("005510009", "BUDAPEST*", new Coordinates(47.51271829, 19.06085762));
    public static final Station STATION_MAGLOD = new Station("005511551", "Maglód", new Coordinates(47.43895881, 19.34071599));

    public static TravelOfferRequest createTravelOfferRequest() {
        return defaultBuilder()
                .build();
    }

    static TravelOfferRequest.Builder defaultBuilder() {
        return TravelOfferRequest.builder()
                .withDeparture(STATION_BUDAPEST_STAR)
                .withDepartureDateTime(LocalDateTime.ofEpochSecond(1571911800L, 0, ZoneOffset.UTC))
                .withDestination(STATION_MAGLOD)
                .withPassengers(new Passenger());
    }
}
