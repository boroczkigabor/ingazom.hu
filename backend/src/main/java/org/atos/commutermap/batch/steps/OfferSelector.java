package org.atos.commutermap.batch.steps;

import org.atos.commutermap.network.model.TravelOffer;
import org.atos.commutermap.network.model.TravelOfferResponse;

public interface OfferSelector {
    TravelOffer selectBestOffer(TravelOfferResponse response);
}
