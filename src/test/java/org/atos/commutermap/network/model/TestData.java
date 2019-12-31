package org.atos.commutermap.network.model;

import org.atos.commutermap.dao.model.Coordinates;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.javamoney.moneta.Money;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestData {

    public static final Station STATION_BAG = new Station("005511155", "Bag", new Coordinates(47.64100555, 19.47165867));
    public static final Station STATION_BUDAPEST_STAR = new Station("005510009", "BUDAPEST*", new Coordinates(47.51271829, 19.06085762));
    public static final Station STATION_BUDAPEST_NYUGATI = new Station("005510017", "Budapest-Keleti", new Coordinates(null, null));
    public static final Station STATION_MAGLOD = new Station("005511551", "Maglód", new Coordinates(47.43895881, 19.34071599));

    public static Route budapestBagRoute(LocalDateTime updateTime) {
        return new Route(STATION_BUDAPEST_STAR, STATION_BAG, STATION_BUDAPEST_NYUGATI, Money.of(465, "HUF"), Duration.ZERO, 15, updateTime);
    }

    public static TravelOfferRequest createTravelOfferRequest() {
        return defaultBuilder()
                .build();
    }

    public static TravelOfferRequest.Builder defaultBuilder() {
        return TravelOfferRequest.builder()
                .withDeparture(STATION_BUDAPEST_STAR)
                .withDepartureDateTime(LocalDateTime.now())
                .withDestination(STATION_MAGLOD)
                .withPassengers(new Passenger());
    }
}
