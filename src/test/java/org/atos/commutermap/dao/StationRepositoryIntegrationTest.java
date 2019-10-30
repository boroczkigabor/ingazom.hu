package org.atos.commutermap.dao;

import org.assertj.core.api.Assertions;
import org.atos.commutermap.dao.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DatabaseConfig.class
})
class StationRepositoryIntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    void stationRepositoryReturnsAllStations() {
        Assertions.assertThat(stationRepository.findAll()).hasSize(1981);
    }
}