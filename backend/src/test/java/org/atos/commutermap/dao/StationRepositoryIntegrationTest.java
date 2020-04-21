package org.atos.commutermap.dao;

import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DatabaseConfig.class
})
@TestPropertySource(locations = "classpath:application.properties")
class StationRepositoryIntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    void stationRepositoryReturnsAllStations() {
        assertThat(stationRepository.findAll()).hasSize(1981);
    }

    @Test
    void findByNameFindsTheStation() {
        Optional<Station> result = stationRepository.findByNameOrId("Abda");
        assertThat(result).isNotEmpty();
        assertThat(result.get().name).isEqualTo("Abda");
        assertThat(result.get().id).isEqualTo("005501305");
        assertThat(result.get().coordinates).isNotNull();
        assertThat(result.get().coordinates.latitude).isEqualTo(47.69074869);
        assertThat(result.get().coordinates.longitude).isEqualTo(17.54090827);
    }

    @Test
    void findByIdFindsTheStation() {
        Optional<Station> result = stationRepository.findByNameOrId("005501305");
        assertThat(result).isNotEmpty();
        assertThat(result.get().name).isEqualTo("Abda");
        assertThat(result.get().id).isEqualTo("005501305");
        assertThat(result.get().coordinates).isNotNull();
        assertThat(result.get().coordinates.latitude).isEqualTo(47.69074869);
        assertThat(result.get().coordinates.longitude).isEqualTo(17.54090827);
    }

}