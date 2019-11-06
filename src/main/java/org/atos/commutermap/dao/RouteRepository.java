package org.atos.commutermap.dao;

import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RouteRepository extends PagingAndSortingRepository<Route, Long> {

    Optional<Route> findByDepartureStationAndDestinationStation(@Param("departureID") Station departure, @Param("destinationID") Station destination);
}
