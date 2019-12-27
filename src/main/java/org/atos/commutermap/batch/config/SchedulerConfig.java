package org.atos.commutermap.batch.config;

import org.springframework.batch.core.Job;
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

import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private Job mavJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 3 * * *")
    public void scheduleJobEveryNight() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobLauncher.run(mavJob, new JobParametersBuilder().addDate("runDate", new Date()).toJobParameters());
    }
}
