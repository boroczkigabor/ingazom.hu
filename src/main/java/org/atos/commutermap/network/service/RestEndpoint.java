package org.atos.commutermap.network.service;

import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.DestinationForMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class RestEndpoint {

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private RouteDurationColorizer routeDurationColorizer;

    @ResponseBody
    @GetMapping(value = "/routes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<Route> getRoutes() {
        return routeRepository.findAll();
    }

    @ResponseBody
    @GetMapping(value = "/destinationsForMap", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<DestinationForMap> destinationsForGoogleMap() {
        return StreamSupport.stream(getRoutes().spliterator(), false)
                .filter(route -> route.destinationStation != null && route.destinationStation.coordinates != null && route.duration != null)
                .map(route ->
                        new DestinationForMap(
                                route.destinationStation.name,
                                route.destinationStation.coordinates.latitude,
                                route.destinationStation.coordinates.longitude,
                                route.duration.toMinutes(),
                                routeDurationColorizer.getColorFor(route)
                        ))
                .collect(Collectors.toList());
    }
}
