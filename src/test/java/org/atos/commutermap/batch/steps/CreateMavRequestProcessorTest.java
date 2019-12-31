package org.atos.commutermap.batch.steps;

import org.atos.commutermap.batch.Util;
import org.atos.commutermap.dao.RouteRepository;
import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.TestData;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.atos.commutermap.network.model.TestData.STATION_BAG;
import static org.atos.commutermap.network.model.TestData.STATION_BUDAPEST_STAR;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMavRequestProcessorTest {

    private CreateMavRequestProcessor processor;

    @Mock
    private StationRepository stationRepository;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private StepExecution stepExecution;
    @Mock
    private JobExecution jobExecution;
    @Mock
    private JobParameters jobParameters;

    @BeforeEach
    void setUp() {
        processor = new CreateMavRequestProcessor(stationRepository, routeRepository, 7);
        processor.beforeStep(stepExecution);
        when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        when(jobExecution.getJobParameters()).thenReturn(jobParameters);
        when(jobParameters.getString(Util.BASE_STATION_ID)).thenReturn(STATION_BUDAPEST_STAR.id);
        when(stationRepository.findById(STATION_BUDAPEST_STAR.id)).thenReturn(Optional.of(STATION_BUDAPEST_STAR));
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