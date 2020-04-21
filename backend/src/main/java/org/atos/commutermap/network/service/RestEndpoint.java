package org.atos.commutermap.network.service;

import com.google.common.collect.ImmutableList;
import org.atos.commutermap.dao.BaseStationRepository;
import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.service.model.BaseStation;
import org.atos.commutermap.network.service.model.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class RestEndpoint implements org.atos.commutermap.network.service.BaseStationsApi, org.atos.commutermap.network.service.DestinationsForMapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestEndpoint.class);

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private BaseStationRepository baseStationRepository;
    @Autowired
    private RouteDurationColorizer routeDurationColorizer;
    @Autowired
    private ElviraUrlCreator elviraUrlCreator;

    @ResponseBody
    @GetMapping(value = "/routes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<Route> getRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return org.atos.commutermap.network.service.BaseStationsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<List<BaseStation>> baseStationsGet() {
        return ResponseEntity.ok(
                ImmutableList.copyOf(baseStations())
        );
    }

    @Override
    public ResponseEntity<List<Destination>> destinationsForMapDepartureStationGet(String departureStation) {
        return ResponseEntity.ok(
                ImmutableList.copyOf(destinationsForGoogleMap(departureStation))
        );
    }

    @ResponseBody
    @GetMapping(value = "/destinationsForMap/{departureStation}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<Destination> destinationsForGoogleMap(@PathVariable("departureStation") String departureStation) {
        Optional<Station> departureStat = stationRepository.findByNameOrId(departureStation);
        if (!departureStat.isPresent()) {
            LOGGER.error("'destinationsForMap' called with unknown station: {}", departureStation);
            throw new IllegalArgumentException("Unknown departure station: " + departureStation);
        }
        return StreamSupport.stream(routeRepository.getAllRoutesByDepartureStation(departureStat.get())
                .spliterator(), false)
                .filter(Route::isReachableWithinTime)
                .map(route ->
                        new Destination()
                                .departure(Optional.ofNullable(route.realDepartureStation).orElse(route.departureStation).name)
                                .destination(route.destinationStation.name)
                                .lat(BigDecimal.valueOf(route.destinationStation.coordinates.latitude))
                                .lon(BigDecimal.valueOf(route.destinationStation.coordinates.longitude))
                                .minutes(String.valueOf(route.duration.toMinutes()))
                                .color(routeDurationColorizer.getColorFor(route))
                                .elviraUrl(elviraUrlCreator.createElviraUrlFor(route)
                        ))
                .collect(Collectors.toList());
    }

    private Iterable<BaseStation> baseStations() {
        return StreamSupport.stream(baseStationRepository.findAll().spliterator(), false)
                .map(baseStation -> new BaseStation()
                        .name(baseStation.name)
                        .id(baseStation.id)
                        .lat(BigDecimal.valueOf(baseStation.coordinates.latitude))
                        .lon(BigDecimal.valueOf(baseStation.coordinates.longitude)
                ))
                .sorted(Comparator.comparing(BaseStation::getName))
                .collect(Collectors.toList());

    }
}
