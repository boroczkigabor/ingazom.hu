package org.atos.commutermap.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class JobStatistics {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStatistics.class);

    private static class JobStatistic {
        private final AtomicInteger numberOfRoutesTotal = new AtomicInteger(0);
        private final AtomicInteger numberOfMavRequests = new AtomicInteger(0);
        private final AtomicInteger filteredDueToBeingTooFar = new AtomicInteger(0);
        private final AtomicInteger filteredDueToLongDuration = new AtomicInteger(0);
        private final AtomicInteger alreadyUpToDate = new AtomicInteger(0);
    }

    private static final JobStatistic jobStatistic = new JobStatistic();

    public static void route() {
        jobStatistic.numberOfRoutesTotal.incrementAndGet();
    }

    public static void mavRequestIssued() {
        jobStatistic.numberOfMavRequests.incrementAndGet();
    }

    public static void filteredAsBeingTooFar() {
        jobStatistic.filteredDueToBeingTooFar.incrementAndGet();
    }

    public static void filteredDueToTooLongTravelling() {
        jobStatistic.filteredDueToLongDuration.incrementAndGet();
    }

    public static void alreadyUpToDate() {
        jobStatistic.alreadyUpToDate.incrementAndGet();
    }

    public static void reset() {
        jobStatistic.alreadyUpToDate.set(0);
        jobStatistic.filteredDueToLongDuration.set(0);
        jobStatistic.filteredDueToBeingTooFar.set(0);
        jobStatistic.numberOfMavRequests.set(0);
        jobStatistic.numberOfRoutesTotal.set(0);
    }

    public static void printStatistics() {
        LOGGER.info("Job completed with the following statistics:\n" +
                    "Number of total routes:                {}\n" +
                    "Number of MAV requests issued:         {}\n" +
                    "Number of routes already up to date:   {}\n" +
                    "Number of destinations too far(dist):  {}\n" +
                    "Number of destinations too far(time):  {}\n",
                jobStatistic.numberOfRoutesTotal, jobStatistic.numberOfMavRequests, jobStatistic.alreadyUpToDate,
                jobStatistic.filteredDueToBeingTooFar, jobStatistic.filteredDueToLongDuration);
    }
}
