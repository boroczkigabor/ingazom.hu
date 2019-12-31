package org.atos.commutermap.batch;

import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Station;
import org.springframework.batch.core.StepExecution;

import java.util.Objects;

public class Util {

    public static final String BASE_STATION_ID = "baseStation";
    public static final String FILTER_LONGER_THAN_KEY = "filterLongerThan";
    public static final String FILTER_FAR_AWAY_STATION_KEY = "filterFarAwayThan";

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static Station getBaseStationFromContext(StepExecution stepExecution, StationRepository stationRepository) {
        Objects.requireNonNull(stepExecution, "stepExecution can't be null");
        Objects.requireNonNull(stationRepository, "stationRepository can't be null");
        String baseStationString = stepExecution.getJobExecution().getJobParameters().getString(BASE_STATION_ID);
        return stationRepository.findById(baseStationString).orElseThrow(() -> new IllegalStateException("Couldn't figure out base station: " + baseStationString));
    }

}
