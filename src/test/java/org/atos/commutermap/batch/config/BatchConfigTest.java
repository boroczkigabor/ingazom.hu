package org.atos.commutermap.batch.config;

import org.atos.commutermap.batch.config.BatchConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        BatchConfig.class,
        SchedulerConfig.class
})
@TestPropertySource(locations = "classpath:application.properties")
class BatchConfigTest {

    @Autowired
    private BatchConfig batchConfig;

    @Test
    void batchConfigMustBeCreatedAndInitialized() {
        assertThat(batchConfig).isNotNull();
    }
}