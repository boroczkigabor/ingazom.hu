package org.atos.commutermap.batch.steps;

import org.atos.commutermap.dao.StationRepository;
import org.atos.commutermap.dao.model.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.atos.commutermap.network.model.TestData.STATION_BUDAPEST_STAR;

@ExtendWith(MockitoExtension.class)
class FilterFarAwayStationsProcessorTest {

    @Mock
    private StationRepository stationRepository;
    private FilterFarAwayStationsProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new FilterFarAwayStationsProcessor(stationRepository);
    }

    @Test
    void calculateDistanceBetweenTheSameStationsMustReturnZero() {
        double result = processor.calculateDistanceBetween(STATION_BUDAPEST_STAR.coordinates, STATION_BUDAPEST_STAR.coordinates);
        assertThat(result).isEqualTo(0d);
    }

    @Test
    void calculateDistanceBetweenTwoOnTheSameLatitudeMustReturnDifferenceInLongitude() {
        Coordinates departure = STATION_BUDAPEST_STAR.coordinates;
        Coordinates destination = new Coordinates(departure.latitude, departure.longitude - 1);

        double result = processor.calculateDistanceBetween(departure, destination);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void calculateDistanceBetweenTwoOnTheSameLongitudeMustReturnDifferenceInLatitude() {
        Coordinates departure = STATION_BUDAPEST_STAR.coordinates;
        Coordinates destination = new Coordinates(departure.latitude -1, departure.longitude);

        double result = processor.calculateDistanceBetween(departure, destination);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void calculateDistanceBetweenOnLongitudeMustReturnDifferenceInLatitude() {
        Coordinates departure = STATION_BUDAPEST_STAR.coordinates;
        Coordinates destination = new Coordinates(departure.latitude -1, departure.longitude - 1);

        double result = processor.calculateDistanceBetween(departure, destination);
        assertThat(result).isEqualTo(2);
    }

}