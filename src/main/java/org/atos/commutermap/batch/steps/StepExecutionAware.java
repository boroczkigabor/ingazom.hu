package org.atos.commutermap.batch.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public abstract class StepExecutionAware extends StepExecutionListenerSupport {

    protected StepExecution stepExecution;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    @BeforeStep
    public final void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        LOGGER.info("Initialized {}", getClass().getSimpleName());
        stepExecutionInitialized(stepExecution);
    }

    protected void stepExecutionInitialized(StepExecution stepExecution) {
        //no-op
    }
}
