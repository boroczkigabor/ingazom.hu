package org.atos.commutermap.batch;

import org.atos.commutermap.dao.model.Route;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

public class FilterFarAwayRoutesProcessor implements ItemProcessor<Route, Route> {

    @Value("${filter.routes.longer.than}")
    private Integer maxDurationInMinutes;

    @Override
    public Route process(Route item) {
        if (item.duration == null || item.duration.toMinutes() > maxDurationInMinutes) {
            return null;
        }

        return item;
    }
}
