package org.atos.commutermap.stationcollector;

import org.atos.commutermap.model.Station;

import java.util.Collection;

public interface StationProvider {
    Collection<Station> getStations();
}
