package org.atos.commutermap.dao;

import org.atos.commutermap.dao.config.RouteDaoConfig;
import org.atos.commutermap.dao.model.Coordinates;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.TestData;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RouteDaoConfig.class
})
@TestPropertySource(locations = "classpath:application.properties")
@Rollback
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
        Station departureStation = new Station("005507229", "", new Coordinates(47d, 17d));
        Station destinationStation = new Station("005511155", "", new Coordinates(47d, 12d));
        Route savedRoute = routeRepository.save(
                new Route(departureStation,
                        destinationStation,
                        null,
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
        Route maglodBudapest = routeRepository.save(
                new Route(TestData.STATION_MAGLOD,
                        TestData.STATION_BUDAPEST_STAR,
                        null,
                        Money.of(123, "HUF"),
                        Duration.of(15, ChronoUnit.MINUTES),
                        1,
                        LocalDateTime.now()));
        maglodBudapest.setDuration(Duration.of(45, ChronoUnit.MINUTES));

        routeRepository.save(maglodBudapest);

        Optional<Route> updatedRoute = routeRepository.findById(maglodBudapest.privateKey());
        assertThat(updatedRoute).isPresent();
        assertThat(updatedRoute.get().getDuration().toMinutes()).isEqualTo(45L);

    }

    @Test
    void getRoutesByDepartureReturnsRoutes() {
        Iterable<Route> budapestRoutes = routeRepository.getAllRoutesByDepartureStation(TestData.STATION_BUDAPEST_STAR);
        assertThat(budapestRoutes).isNotEmpty();
    }
}