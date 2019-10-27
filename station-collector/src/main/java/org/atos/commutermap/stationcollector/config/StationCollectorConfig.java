package org.atos.commutermap.stationcollector.config;

import org.atos.commutermap.stationcollector.DatabaseStationProvider;
import org.atos.commutermap.stationcollector.StationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import(DatabaseConfig.class)
@Configuration
public class StationCollectorConfig {

    @Bean
    public StationProvider databaseStationProvider(@Autowired JdbcTemplate jdbcTemplate) {
        return new DatabaseStationProvider(jdbcTemplate);
    }
}
