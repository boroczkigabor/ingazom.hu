package org.atos.commutermap.batch;

import com.google.common.collect.ImmutableSortedSet;
import org.atos.commutermap.dao.model.Route;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.springframework.batch.item.ItemProcessor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Random;

public class CallMavProcessor implements ItemProcessor<TravelOfferRequest, Route> {

    private final MavinfoServerCaller serverCaller;
    private Random random = new Random();

    public CallMavProcessor(MavinfoServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    @Override
    public Route process(TravelOfferRequest request) {
        TravelOfferResponse response = serverCaller.callServerWith(request);

        ImmutableSortedSet<TravelOffer> sortedOffers = ImmutableSortedSet
                .<TravelOffer>orderedBy(Comparator.comparing(o -> o.travelTime))
                .addAll(response.travelOffers)
                .build();

        TravelOffer firstOffer = sortedOffers.first();

        Duration randomDurationLessThanHour = Duration.of(random.nextInt(60), ChronoUnit.MINUTES);
        return new Route(request.departure, request.destination, firstOffer.price, randomDurationLessThanHour, firstOffer.distance, LocalDateTime.now());
    }
}
