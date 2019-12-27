package org.atos.commutermap.batch.steps;

import org.atos.commutermap.dao.model.Coordinates;
import org.atos.commutermap.dao.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import static java.util.Objects.requireNonNull;

public class FilterFarAwayStationsProcessor implements ItemProcessor<Station, Station> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterFarAwayStationsProcessor.class);

    private final Station departureStation;
    @Value("${filter.stations.closer.than}")
    private double maxDistance;

    public FilterFarAwayStationsProcessor(Station departureStation) {
        this.departureStation = departureStation;
        requireNonNull(departureStation.coordinates, "Coordinates for the departure station can't be null!");
    }

    @Override
    public Station process(Station item) throws Exception {
        requireNonNull(item.coordinates, "Item's coordinates can't be null: " + item.toString());
        Coordinates departureCoordinates = departureStation.coordinates;
        Coordinates destinationCoordinates = item.coordinates;

        double distancePower = calculateDistanceBetween(departureCoordinates, destinationCoordinates);
        LOGGER.trace("{} <---> {} - Calculated distance: {}", departureStation.name, item.name, Math.sqrt(distancePower));
        if (distancePower > Math.pow(maxDistance, 2)) {
            LOGGER.info("Skipping {} as it is too far from the departure station - {}.", item.name, Math.sqrt(distancePower));
            return null;
        } else {
            return item;
        }
    }

    double calculateDistanceBetween(Coordinates departure, Coordinates destination) {
        return Math.pow(departure.latitude - destination.latitude, 2) +
               Math.pow(departure.longitude - destination.longitude, 2);
    }
}
