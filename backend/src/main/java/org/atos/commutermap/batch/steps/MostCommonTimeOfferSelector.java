package org.atos.commutermap.batch.steps;

import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class MostCommonTimeOfferSelector implements OfferSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MostCommonTimeOfferSelector.class);

    @Override
    public TravelOffer selectBestOffer(TravelOfferResponse response) {
        if (response.travelOffers.isEmpty()) {
            throw new NoSuchElementException("There aren't any travel offers in the response");
        }

        Optional<Map.Entry<Long, List<TravelOffer>>> bestOffer = response.travelOffers.stream()
                .collect(groupingBy(travelOffer -> travelOffer.travelTime.toMinutes()))
                .entrySet().stream()
                .min(Comparator.<Map.Entry<Long, List<TravelOffer>>>
                         comparingInt(e -> e.getValue().size())
                         .reversed() //the biggest occurrence of the duration the better
                        .thenComparingLong(Map.Entry::getKey)); //the smallest duration the better

        if (!bestOffer.isPresent() || bestOffer.get().getValue().isEmpty()) {
            //unable to reach in theory
            LOGGER.error("Unable to figure out the best offer. Lambda returned {}", bestOffer);
            throw new NoSuchElementException("Unable to find best offer");
        }

        return bestOffer.get().getValue().get(0);
    }
}
