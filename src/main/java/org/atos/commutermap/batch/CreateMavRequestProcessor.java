package org.atos.commutermap.batch;

import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.Passenger;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateMavRequestProcessor implements ItemProcessor<Station, TravelOfferRequest> {

    private final Station baseStation;

    public CreateMavRequestProcessor(Station baseStation) {
        this.baseStation = baseStation;
    }

    @Override
    public TravelOfferRequest process(Station item) {
        return TravelOfferRequest.builder()
                .withDeparture(baseStation)
                .withDestination(item)
                .withPassengers(new Passenger())
                .withDepartureDateTime(LocalDate.now().atTime(LocalTime.MIDNIGHT))
                .build();
    }
}
