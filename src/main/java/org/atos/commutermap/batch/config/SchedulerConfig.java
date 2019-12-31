package org.atos.commutermap.batch.config;

import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.BaseStationRepository;
import org.atos.commutermap.dao.model.BaseStation;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    private BaseStationRepository baseStationRepository;

    @Autowired
    private Job mavJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 3 * * *")
    public void scheduleJobsEveryNight() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        for (BaseStation baseStation : baseStationRepository.findAll()) {
            startJobFor(baseStation);
        }
    }

    private void startJobFor(BaseStation baseStation) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("runDate", LocalDate.now().toEpochDay())
                .addString(Util.BASE_STATION_ID, baseStation.id)
                .addLong(Util.FILTER_LONGER_THAN_KEY, (long) baseStation.maxDuration)
                .addDouble(Util.FILTER_FAR_AWAY_STATION_KEY, baseStation.maxDistance)
                .toJobParameters();
        LOGGER.info("Starting mavJob with the following parameters: {}", jobParameters.toString());
        jobLauncher.run(mavJob,
                jobParameters);
    }
}
