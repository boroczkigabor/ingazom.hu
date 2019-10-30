package org.atos.commutermap.stationcollector.service;

import org.atos.commutermap.dao.model.Station;

public interface StationProvider {
    Iterable<Station> getStations();
}
