package org.atos.commutermap.network.service;

import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;

public interface MavinfoServerCaller {
    TravelOfferResponse callServerWith(TravelOfferRequest payload);
}
