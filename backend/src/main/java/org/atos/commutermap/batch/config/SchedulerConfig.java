package org.atos.commutermap.batch.config;

import com.google.common.collect.ImmutableList;
import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.BaseStationRepository;
import org.atos.commutermap.dao.model.BaseStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public String scheduleJobsEveryNight() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException {
        ImmutableList.Builder<JobExecution> builder = ImmutableList.builder();
        for (BaseStation baseStation : baseStationRepository.findAll()) {
            startJobFor(baseStation).ifPresent(builder::add);
        }

        return builder.build().toString();
    }

    public String startJobFor(String baseStation) {
        Optional<BaseStation> station = baseStationRepository.findByName(baseStation);
        if (station.isPresent()) {
            return startJobFor(station.get()).map(JobExecution::toString).orElse("ERROR");
        } else {
            throw new NoSuchElementException("No such baseStation: " + baseStation);
        }
    }

    private Optional<JobExecution> startJobFor(BaseStation baseStation) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("runDate", LocalDate.now().toEpochDay())
                .addString(Util.BASE_STATION_ID, baseStation.id)
                .addLong(Util.FILTER_LONGER_THAN_KEY, (long) baseStation.maxDuration)
                .addDouble(Util.FILTER_FAR_AWAY_STATION_KEY, baseStation.maxDistance)
                .toJobParameters();
        LOGGER.info("Starting mavJob with the following parameters: {}", jobParameters.toString());
        try {
            return Optional.of(jobLauncher.run(mavJob, jobParameters));
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException e) {
            LOGGER.error("Skipping job for {} as it has been already completed today.", baseStation.name);
            return Optional.empty();
        } catch (JobExecutionException e) {
            LOGGER.error("Error occurred job execution start:", e);
            throw new RuntimeException(e);
        }
    }
}
