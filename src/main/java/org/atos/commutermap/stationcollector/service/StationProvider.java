package org.atos.commutermap.stationcollector.service;

import org.atos.commutermap.dao.model.Station;

import java.util.Collection;

public interface StationProvider {
    Collection<Station> getStations();
}
