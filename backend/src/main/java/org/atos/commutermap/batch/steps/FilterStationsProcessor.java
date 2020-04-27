package org.atos.commutermap.batch.steps;

import org.atos.commutermap.batch.JobStatistics;
import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FilterStationsProcessor extends StepExecutionAware implements ItemProcessor<Station, Station> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMavRequestProcessor.class);
    private final StationRepository stationRepository;

    public FilterStationsProcessor(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Station process(Station item) {
        Station baseStation = Util.getBaseStationFromContext(stepExecution, stationRepository);
        if (item.coordinates == null || item.coordinates.longitude == null) {
            LOGGER.trace("Skipping station {} due to missing coordinates.", item.name);
            return null;
        } else if (baseStation.equals(item)) {
            return null;
        }
        JobStatistics.route();
        return item;
    }
}
