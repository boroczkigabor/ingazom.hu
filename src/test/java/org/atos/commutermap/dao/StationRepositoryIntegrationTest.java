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
        Optional<Station> result = stationRepository.findByName("BUDAPEST*");
        assertThat(result).isNotEmpty();
        assertThat(result.get().name).isEqualTo("BUDAPEST*");
        assertThat(result.get().id).isEqualTo("005510009");
    }
}