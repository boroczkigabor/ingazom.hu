package org.atos.commutermap.batch.steps;

import org.atos.commutermap.batch.JobStatistics;
import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Coordinates;
import org.atos.commutermap.dao.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.item.ItemProcessor;

import static java.util.Objects.requireNonNull;

public class FilterFarAwayStationsProcessor extends StepExecutionAware implements ItemProcessor<Station, Station> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterFarAwayStationsProcessor.class);

    private final StationRepository stationRepository;

    public FilterFarAwayStationsProcessor(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void check(Station station) {
        requireNonNull(station.coordinates, "Coordinates can't be null! " + station.name);
    }

    @Override
    public Station process(Station item) {
        check(item);
        Station baseStation = Util.getBaseStationFromContext(stepExecution, stationRepository);
        check(baseStation);
        Coordinates departureCoordinates = baseStation.coordinates;
        Coordinates destinationCoordinates = item.coordinates;
        double maxDistance = stepExecution.getJobExecution().getJobParameters().getDouble(Util.FILTER_FAR_AWAY_STATION_KEY);

        double distancePower = calculateDistanceBetween(departureCoordinates, destinationCoordinates);
        LOGGER.trace("{} <---> {} - Calculated distance: {}", baseStation.name, item.name, Math.sqrt(distancePower));
        if (distancePower > Math.pow(maxDistance, 2)) {
            LOGGER.info("Skipping {} as it is too far from the departure station - {}.", item.name, Math.sqrt(distancePower));
            JobStatistics.filteredAsBeingTooFar();
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
