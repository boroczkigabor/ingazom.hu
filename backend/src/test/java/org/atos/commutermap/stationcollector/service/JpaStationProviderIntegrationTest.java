package org.atos.commutermap.stationcollector.service;

import org.assertj.core.api.Assertions;
import org.atos.commutermap.dao.config.RouteDaoConfig;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.stationcollector.config.StationCollectorConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration( classes = {
        StationCollectorConfig.class,
        RouteDaoConfig.class
})
@TestPropertySource(locations = "classpath:application.properties")
class JpaStationProviderIntegrationTest {

    @Autowired
    private StationProvider stationProvider;

    @Test
    void stationProviderMustReturnDataFromDatabase() {
        Iterable<Station> stations = stationProvider.getStations();
        Assertions.assertThat(stations).isNotEmpty();
        stations.forEach(System.out::println);
    }
}