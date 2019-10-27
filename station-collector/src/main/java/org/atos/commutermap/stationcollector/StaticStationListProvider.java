package org.atos.commutermap.stationcollector;

import com.google.common.collect.ImmutableList;
import org.atos.commutermap.stationcollector.model.Station;

import java.util.Collection;

public class StaticStationListProvider implements StationProvider {

    @Override
    public Collection<Station> getStations() {
        return ImmutableList.of(
                new Station("005510009", "BUDAPEST*"),
                new Station("005511551", "Maglod")
        );
    }
}
