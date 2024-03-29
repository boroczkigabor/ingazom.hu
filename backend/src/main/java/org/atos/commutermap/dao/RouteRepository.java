package org.atos.commutermap.dao;

import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RouteRepository extends PagingAndSortingRepository<Route, Route.RoutePK> {

    Iterable<Route> getAllRoutesByDepartureStation(Station departureStation);
}
