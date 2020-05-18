package org.atos.commutermap.network.service;

import org.atos.commutermap.batch.config.SchedulerConfig;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigurationEndpoint {

    @Autowired
    private SchedulerConfig schedulerConfig;

    @Secured("ROLE_ADMIN")
    @GetMapping("config/startJobs")
    @ResponseBody
    public String startJobsNow() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return schedulerConfig.scheduleJobsEveryNight();
    }
}
