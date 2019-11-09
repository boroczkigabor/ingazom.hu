package org.atos.commutermap.batch;

import com.google.common.collect.ImmutableSortedSet;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;

public class CallMavProcessor implements ItemProcessor<TravelOfferRequest, Route> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallMavProcessor.class);

    private final MavinfoServerCaller serverCaller;

    public CallMavProcessor(MavinfoServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    @Override
    public Route process(TravelOfferRequest request) throws IOException {
        LOGGER.info("Calling MAV for {} - {}.", request.departure.name, request.destination.name);
        TravelOfferResponse response = serverCaller.callServerWith(request);
        if (!response.errorMessages.isEmpty() || response.travelOffers.isEmpty()) {
            boolean exhausted = response.errorMessages
                    .stream()
                    .anyMatch(msg -> "0".equals(msg.ID));
            if (exhausted) {
                LOGGER.warn("MAV server got exhausted, waiting 30 seconds...");
                Util.sleep(30_000);
                return process(request);
            } else {
                LOGGER.info("Skipping {} - {} as response is empty.", request.departure.name, request.destination.name);
                return null;
            }
        }
        ImmutableSortedSet<TravelOffer> sortedOffers = ImmutableSortedSet
                .<TravelOffer>orderedBy(Comparator.comparing(o -> o.travelTime))
                .addAll(response.travelOffers)
                .build();

        TravelOffer firstOffer = sortedOffers.first();

        return new Route(request.departure, request.destination, firstOffer.price, firstOffer.travelTime, firstOffer.distance, LocalDateTime.now());
    }
}
