package org.atos.commutermap.batch.steps;

import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.Passenger;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Optional;

import static java.time.DayOfWeek.MONDAY;

public class CreateMavRequestProcessor implements ItemProcessor<Station, TravelOfferRequest> {

    private final Station baseStation;
    private final RouteRepository routeRepository;
    private final Period outdatedPeriod;

    public CreateMavRequestProcessor(Station baseStation, RouteRepository routeRepository, int dataBecomesStaleAfter_days) {
        this.baseStation = baseStation;
        this.routeRepository = routeRepository;
        outdatedPeriod = Period.ofDays(dataBecomesStaleAfter_days).normalized();
    }

    @Override
    public TravelOfferRequest process(Station item) {
        Optional<Route> potentiallyPresentRoute = routeRepository.findById(new Route.RoutePK(baseStation.id, item.id));
        if (potentiallyPresentRoute.isPresent() && potentiallyPresentRoute.get().updateTime.plus(outdatedPeriod).isAfter(LocalDateTime.now())) {
            LoggerFactory.getLogger(getClass()).info("Skipping station {} as its route is already present and needs no update.", item.name);
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
