package org.atos.commutermap.dao;

import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.Coordinates;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.TestData;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DatabaseConfig.class
})
class RouteRepositoryIntegrationTest {

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void routeRepositoryMustBeAbleToRetrieveData() {
        Iterable<Route> allRoutes = routeRepository.findAll();

        assertThat(allRoutes).isNotEmpty();
    }

    @Transactional
    @Test
    void routeRepositoryMustBeAbleToSaveData() {
        Station departureStation = new Station("005507229", "", new Coordinates(47L, 17L));
        Station destinationStation = new Station("005511155", "", new Coordinates(47L, 12L));
        Route savedRoute = routeRepository.save(
                new Route(departureStation,
                        destinationStation,
                        Money.of(123, "HUF"),
                        Duration.of(10, ChronoUnit.MINUTES),
                        1,
                        LocalDateTime.now()));

        assertThat(routeRepository.findById(new Route.RoutePK(departureStation.id, destinationStation.id)))
                .isNotEmpty()
                .contains(savedRoute);
    }

    @Test
    void routeRepositoryMustBeAbleToUpdateData() {
        Route savedRoute = routeRepository.save(
                new Route(TestData.STATION_BUDAPEST_STAR,
                        TestData.STATION_MAGLOD,
                        Money.of(123, "HUF"),
                        Duration.of(15, ChronoUnit.MINUTES),
                        1,
                        LocalDateTime.now()));
        //save once again which is an update
        routeRepository.save(savedRoute);

    }
}