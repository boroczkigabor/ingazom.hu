package org.atos.commutermap.batch.steps;

import org.atos.commutermap.dao.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FilterStationsProcessor implements ItemProcessor<Station, Station> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMavRequestProcessor.class);
    private final Station baseStation;

    public FilterStationsProcessor(Station baseStation) {
        this.baseStation = baseStation;
    }

    @Override
    public Station process(Station item) {
        if (item.coordinates == null || item.coordinates.longitude == null) {
            LOGGER.trace("Skipping station {} due to missing coordinates.", item.name);
            return null;
        } else if (baseStation.equals(item)) {
            return null;
        }

        return item;
    }
}
