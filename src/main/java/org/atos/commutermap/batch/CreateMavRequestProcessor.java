package org.atos.commutermap.batch;

import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.Passenger;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static java.time.DayOfWeek.MONDAY;

public class CreateMavRequestProcessor implements ItemProcessor<Station, TravelOfferRequest> {

    private final Station baseStation;
    private final RouteRepository routeRepository;

    public CreateMavRequestProcessor(Station baseStation, RouteRepository routeRepository) {
        this.baseStation = baseStation;
        this.routeRepository = routeRepository;
    }

    @Override
    public TravelOfferRequest process(Station item) {
        Optional<Route> potentiallyPresentRoute = routeRepository.findById(new Route.RoutePK(baseStation.id, item.id));
        if (potentiallyPresentRoute.isPresent()) {
            LoggerFactory.getLogger(getClass()).info("Skipping station {} as its route is already present.", item.name);
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.minusDays(today.getDayOfWeek().getValue() - MONDAY.getValue())
                                    .plusWeeks(1);
        return TravelOfferRequest.builder()
                .withDeparture(baseStation)
                .withDestination(item)
                .withPassengers(new Passenger())
                .withDepartureDateTime(nextMonday.atTime(LocalTime.MIDNIGHT))
                .build();
    }
}
