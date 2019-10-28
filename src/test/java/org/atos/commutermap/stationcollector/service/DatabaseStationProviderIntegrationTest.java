package org.atos.commutermap.stationcollector.service;

import org.assertj.core.api.Assertions;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.stationcollector.config.StationCollectorConfig;
import org.atos.commutermap.stationcollector.service.StationProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

@ExtendWith(SpringExtension.class)
@ContextConfiguration( classes = {
        StationCollectorConfig.class
})
class DatabaseStationProviderIntegrationTest {

    @Autowired
    private StationProvider stationProvider;

    @Test
    void stationProviderMustReturnDataFromDatabase() {
        Collection<Station> stations = stationProvider.getStations();
        Assertions.assertThat(stations).isNotEmpty();
        stations.forEach(System.out::println);
    }
}