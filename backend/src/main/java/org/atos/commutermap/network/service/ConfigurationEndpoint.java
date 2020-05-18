package org.atos.commutermap.network.service;

import org.atos.commutermap.batch.config.SchedulerConfig;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("config")
public class ConfigurationEndpoint {

    @Autowired
    private SchedulerConfig schedulerConfig;

    @PostMapping("jobs")
    @ResponseBody
    public String startJobsNow() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return schedulerConfig.scheduleJobsEveryNight();
    }

    @PostMapping("jobs/{baseStation}")
    @ResponseBody
    public String startJobNowFor(@PathVariable("baseStation") String baseStationName) {
        return schedulerConfig.startJobFor(baseStationName);
    }

}
