package org.atos.commutermap.stationcollector;

import org.atos.commutermap.stationcollector.model.Station;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DatabaseStationProvider implements StationProvider {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseStationProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Station> getStations() {
        return jdbcTemplate.query("SELECT * FROM station",
                (rs, rowNum) -> new Station(rs.getString("id"),
                                            rs.getString("name")));
    }
}
