package org.atos.commutermap.batch.steps;

import org.atos.commutermap.batch.steps.CreateMavRequestProcessor;
import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.TestData;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.atos.commutermap.network.model.TestData.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMavRequestProcessorTest {

    private CreateMavRequestProcessor processor;

    @Mock
    private RouteRepository routeRepository;

    @BeforeEach
    void setUp() {
        processor = new CreateMavRequestProcessor(STATION_BUDAPEST_STAR, routeRepository, 7);
    }

    @Test
    void processWhenRouteDoesntExistMustReturnRequest() {
        when(routeRepository.findById(pkBudapestBag()))
                .thenReturn(Optional.empty());
        TravelOfferRequest request = processor.process(STATION_BAG);

        assertThat(request).isNotNull();
        assertThat(request.departure).isEqualTo(STATION_BUDAPEST_STAR);
        assertThat(request.destination).isEqualTo(STATION_BAG);
    }

    @Test
    void processWhenRouteExistsAndUpToDateMustReturnNull() {
        when(routeRepository.findById(pkBudapestBag()))
                .thenReturn(Optional.of(TestData.budapestBagRoute(LocalDateTime.now().minusDays(6))));

        TravelOfferRequest request = processor.process(STATION_BAG);
        assertThat(request).isNull();
    }

    @Test
    void processWhenRouteExistsButOutdatedMustReturnRequest() {
        when(routeRepository.findById(pkBudapestBag()))
                .thenReturn(Optional.of(TestData.budapestBagRoute(LocalDateTime.now().minusDays(8))));

        TravelOfferRequest request = processor.process(STATION_BAG);
        assertThat(request).isNotNull();
    }

    private Route.RoutePK pkBudapestBag() {
        return Mockito.eq(new Route.RoutePK(STATION_BUDAPEST_STAR.id, STATION_BAG.id));
    }
}