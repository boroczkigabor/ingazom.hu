package org.atos.commutermap.network.service;

import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.DestinationForMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class RestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestEndpoint.class);

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private RouteDurationColorizer routeDurationColorizer;
    @Autowired
    private ElviraUrlCreator elviraUrlCreator;

    @ResponseBody
    @GetMapping(value = "/routes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<Route> getRoutes() {
        return routeRepository.findAll();
    }

    @ResponseBody
    @GetMapping(value = "/destinationsForMap/{departureStation}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DestinationForMap> destinationsForGoogleMap(@PathVariable("departureStation") String departureStation) {
        Optional<Station> departureStat = stationRepository.findById(departureStation);
        if (!departureStat.isPresent()) {
            LOGGER.error("'destinationsForMap' called with unknown station: {}", departureStation);
            throw new IllegalArgumentException("Unknown departure station: " + departureStation);
        }
        return StreamSupport.stream(routeRepository.getAllRoutesByDepartureStation(departureStat.get())
                .spliterator(), false)
                .filter(Route::isReachableWithinTime)
                .map(route ->
                        new DestinationForMap(
                                Optional.ofNullable(route.realDepartureStation).orElse(route.departureStation).name,
                                route.destinationStation.name,
                                route.destinationStation.coordinates.latitude,
                                route.destinationStation.coordinates.longitude,
                                route.duration.toMinutes(),
                                routeDurationColorizer.getColorFor(route),
                                elviraUrlCreator.createElviraUrlFor(route)
                        ))
                .collect(Collectors.toList());
    }
}
