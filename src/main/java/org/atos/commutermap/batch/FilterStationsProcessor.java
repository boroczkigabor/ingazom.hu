package org.atos.commutermap.batch;

import org.atos.commutermap.dao.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FilterStationsProcessor implements ItemProcessor<Station, Station> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMavRequestProcessor.class);

    @Override
    public Station process(Station item) {
        if (item.coordinates == null || item.coordinates.longitude == null) {
            LOGGER.info("Skipping station {} due to missing coordinates.", item.name);
            return null;
        }

        return item;
    }
}
