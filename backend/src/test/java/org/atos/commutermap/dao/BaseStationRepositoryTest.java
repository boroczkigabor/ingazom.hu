package org.atos.commutermap.dao;

import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.BaseStation;
import org.atos.commutermap.network.model.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DatabaseConfig.class
})
@TestPropertySource(locations = "classpath:application.properties")
class BaseStationRepositoryTest {

    @Autowired
    private BaseStationRepository repository;

    @Test
    void repositoryMustReturnById() {
        Optional<BaseStation> allBaseStations = repository.findById(TestData.STATION_BUDAPEST_STAR.id);

        assertThat(allBaseStations).isNotEmpty();
        BaseStation baseStation = allBaseStations.get();
        assertThat(baseStation.maxDistance).isEqualTo(0.75d);
        assertThat(baseStation.maxDuration).isEqualTo(60);
        assertThat(baseStation.id).isEqualTo(TestData.STATION_BUDAPEST_STAR.id);
        assertThat(baseStation.name).isEqualTo(TestData.STATION_BUDAPEST_STAR.name);
        assertThat(baseStation.coordinates).isEqualTo(TestData.STATION_BUDAPEST_STAR.coordinates);
    }

    @Test
    void repositoryMustReturnAllBaseStations() {
        Iterable<BaseStation> all = repository.findAll();

        assertThat(all).isNotEmpty();
    }
}