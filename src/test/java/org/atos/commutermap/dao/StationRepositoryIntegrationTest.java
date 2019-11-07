package org.atos.commutermap.dao;

import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

    @ExtendWith(SpringExtension.class)
    @ContextConfiguration(classes = {
            DatabaseConfig.class
    })
class StationRepositoryIntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    void stationRepositoryReturnsAllStations() {
        assertThat(stationRepository.findAll()).hasSize(1981);
    }

    @Test
    void findByNameFindsTheStation() {
        Optional<Station> result = stationRepository.findByName("Abda");
        assertThat(result).isNotEmpty();
        assertThat(result.get().name).isEqualTo("Abda");
        assertThat(result.get().id).isEqualTo("005501305");
        assertThat(result.get().coordinates).isNotNull();
        assertThat(result.get().coordinates.latitude).isEqualTo(4769074869L);
        assertThat(result.get().coordinates.longitude).isEqualTo(1754090827);
    }
}