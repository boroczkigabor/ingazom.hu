package org.atos.commutermap.dao;

import org.atos.commutermap.dao.config.DatabaseConfig;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.dao.model.Station;
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

        assertThat(allRoutes).hasSize(1);
    }

    @Transactional
    @Test
    void routeRepositoryMustBeAbleToSaveData() {
        Route savedRoute = routeRepository.save(
                new Route(new Station("005507229", ""),
                        new Station("005513300", ""),
                        Money.of(123, "HUF"),
                        Duration.of(10, ChronoUnit.MINUTES),
                        1,
                        LocalDateTime.now()));

        assertThat(routeRepository.findById(savedRoute.id))
                .isNotEmpty()
                .contains(savedRoute);
    }
}