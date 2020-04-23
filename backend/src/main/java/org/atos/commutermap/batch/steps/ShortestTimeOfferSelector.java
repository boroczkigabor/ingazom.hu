package org.atos.commutermap.batch.steps;

import com.google.common.collect.ImmutableSortedSet;
import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferResponse;

import java.util.Comparator;

public class ShortestTimeOfferSelector implements OfferSelector {
    public ShortestTimeOfferSelector() {
    }

    @Override
    public TravelOffer selectBestOffer(TravelOfferResponse response) {
        ImmutableSortedSet<TravelOffer> sortedOffers = ImmutableSortedSet
                .<TravelOffer>orderedBy(Comparator.comparing(o -> o.travelTime))
                .addAll(response.travelOffers)
                .build();

        return sortedOffers.first();
    }
}