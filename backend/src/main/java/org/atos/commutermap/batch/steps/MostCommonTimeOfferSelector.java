package org.atos.commutermap.batch.steps;

import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MostCommonTimeOfferSelector implements OfferSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MostCommonTimeOfferSelector.class);

    @Override
    public TravelOffer selectBestOffer(TravelOfferResponse response) {
        if (response.travelOffers.isEmpty()) {
            throw new NoSuchElementException("There aren't any travel offers in the response");
        }

        Optional<Map.Entry<Long, ArrayList<TravelOffer>>> bestOffer = response.travelOffers.stream()
                .collect(Collectors.toMap(
                        travelOffer -> travelOffer.travelTime.toMinutes(),
                        travelOffer -> new ArrayList<>(Collections.singletonList(travelOffer)),
                        (oldValue, newValue) -> {
                            oldValue.addAll(newValue);
                            return oldValue;
                        }
                )).entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()));

        if (!bestOffer.isPresent() || bestOffer.get().getValue().isEmpty()) {
            LOGGER.error("Unable to figure out the best offer. Lambda returned {}", bestOffer);
            throw new NoSuchElementException("Unable to find best offer");
        }

        return bestOffer.get().getValue().get(0);
    }
}
