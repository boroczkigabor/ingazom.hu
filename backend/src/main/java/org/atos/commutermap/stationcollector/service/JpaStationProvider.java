package org.atos.commutermap.stationcollector.service;

import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Station;

public class JpaStationProvider implements StationProvider {

    private final StationRepository stationRepository;

    public JpaStationProvider(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Iterable<Station> getStations() {
        return stationRepository.findAll();
    }
}
