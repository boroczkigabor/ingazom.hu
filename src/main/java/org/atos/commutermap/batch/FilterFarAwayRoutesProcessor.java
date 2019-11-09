package org.atos.commutermap.batch;

import org.atos.commutermap.dao.model.Route;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

public class FilterFarAwayRoutesProcessor implements ItemProcessor<Route, Route> {

    @Value("${filter.routes.longer.than}")
    private Integer maxDurationInMinutes;

    @Override
    public Route process(Route item) {
        if (item.duration == null || item.duration.toMinutes() > maxDurationInMinutes) {
            LoggerFactory.getLogger(getClass()).info("Filtering {} as duration is greater than maximum: {} > {}", item.destinationStation.name, item.duration == null ? null : item.duration.toMinutes(), maxDurationInMinutes);
            return null;
        }

        return item;
    }
}
