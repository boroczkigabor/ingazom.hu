package org.atos.commutermap.batch.steps;

import org.atos.commutermap.batch.JobStatistics;
import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.model.Route;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.env.Environment;

public class MarkFarAwayRoutesProcessor extends StepExecutionAware implements ItemProcessor<Route, Route> {

    @Override
    public Route process(Route item) {
        Long maxDurationInMinutes = stepExecution.getJobExecution().getJobParameters().getLong(Util.FILTER_LONGER_THAN_KEY);
        if (item.duration == null || item.duration.toMinutes() > maxDurationInMinutes) {
            LoggerFactory.getLogger(getClass()).info("Marking {} as far away because duration is greater than maximum: {} > {}", item.destinationStation.name, item.duration == null ? null : item.duration.toMinutes(), maxDurationInMinutes);
            JobStatistics.filteredDueToTooLongTravelling();
            item.markFarAway();
        }

        return item;
    }
}
