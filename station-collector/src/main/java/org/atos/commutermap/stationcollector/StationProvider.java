package org.atos.commutermap.stationcollector;

import org.atos.commutermap.stationcollector.model.Station;

import java.util.Collection;

public interface StationProvider {
    Collection<Station> getStations();
}
