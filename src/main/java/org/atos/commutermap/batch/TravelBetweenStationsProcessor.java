package org.atos.commutermap.batch;

import org.atos.commutermap.dao.model.Station;
import org.atos.commutermap.network.model.Passenger;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalTime;

public class TravelBetweenStationsProcessor implements ItemProcessor<Station, TravelOfferResponse> {

    private final Station baseStation;
    private final MavinfoServerCaller serverCaller;

    public TravelBetweenStationsProcessor(Station baseStation, MavinfoServerCaller serverCaller) {
        this.baseStation = baseStation;
        this.serverCaller = serverCaller;
    }

    @Override
    public TravelOfferResponse process(Station item) {
        TravelOfferRequest request = TravelOfferRequest.builder()
                .withDeparture(baseStation)
                .withDestination(item)
                .withPassengers(new Passenger())
                .withDepartureDateTime(LocalDate.now().atTime(LocalTime.MIDNIGHT))
                .build();

        return serverCaller.callServerWith(request);
    }
}
