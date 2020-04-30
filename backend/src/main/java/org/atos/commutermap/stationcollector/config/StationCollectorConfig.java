package org.atos.commutermap.stationcollector.config;

import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.common.config.DatabaseConfig;
import org.atos.commutermap.stationcollector.service.JpaStationProvider;
import org.atos.commutermap.stationcollector.service.StationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(DatabaseConfig.class)
@Configuration
public class StationCollectorConfig {

    @Bean
    public StationProvider databaseStationProvider(@Autowired StationRepository stationRepository) {
        return new JpaStationProvider(stationRepository);
    }
}
