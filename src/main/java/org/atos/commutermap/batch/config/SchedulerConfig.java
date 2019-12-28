package org.atos.commutermap.batch.config;

import org.atos.commutermap.batch.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfig.class);

    @Value("${configuration.baseStations}")
    private String[] baseStations;

    @Autowired
    private Environment env;

    @Autowired
    private Job mavJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "0 * * * * *")
    public void scheduleJobsEveryNight() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        for (String baseStation : baseStations) {
            startJobFor(baseStation);
        }
    }

    private void startJobFor(String baseStation) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("runDate", LocalDate.now().toEpochDay())
                .addString(Util.BASE_STATION_KEY, baseStation)
                .addLong(Util.FILTER_LONGER_THAN_KEY, env.getRequiredProperty(String.format("%s.%s", "filter.routes.longer.than", baseStation), Long.class))
                .addDouble(Util.FILTER_FAR_AWAY_STATION_KEY, env.getRequiredProperty(String.format("%s.%s", "filter.stations.farther.than", baseStation), Double.class))
                .toJobParameters();
        LOGGER.info("Starting mavJob with the following parameters: {}", jobParameters.toString());
        jobLauncher.run(mavJob,
                jobParameters);
    }
}
