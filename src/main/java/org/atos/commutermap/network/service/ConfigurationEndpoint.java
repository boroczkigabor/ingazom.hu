package org.atos.commutermap.network.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ConfigurationEndpoint {

    @Autowired
    private Job mavJob;

    @Autowired
    private JobRepository jobRepository;

    private SimpleJobLauncher launcher;

    public JobLauncher getLauncher() {
        if (launcher == null) {
            launcher = new SimpleJobLauncher();
            launcher.setJobRepository(jobRepository);
            launcher.setTaskExecutor(new SimpleAsyncTaskExecutor("batch"));
        }
        return launcher;
    }

    @GetMapping("config/startJob")
    @ResponseBody
    public String startJobNow() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution jobExecution = getLauncher().run(mavJob,
                new JobParametersBuilder()
                        .addDate("startedAt", new Date())
                        .toJobParameters());
        return jobExecution.toString();
    }
}
