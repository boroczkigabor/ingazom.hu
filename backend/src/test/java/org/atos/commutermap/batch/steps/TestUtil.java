package org.atos.commutermap.batch.steps;

import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferDetails;

class TestUtil {
    private TestUtil() {}

    static TravelOffer createTravelOffer(String travelTime) {
        return new TravelOffer(1L, 2L, travelTime, 5, 450, true, new TravelOfferDetails[]{});
    }
}
