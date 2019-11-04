package org.atos.commutermap.batch;

import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.springframework.batch.item.ItemProcessor;

public class CallMavProcessor implements ItemProcessor<TravelOfferRequest, TravelOfferResponse> {

    private final MavinfoServerCaller serverCaller;

    public CallMavProcessor(MavinfoServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    @Override
    public TravelOfferResponse process(TravelOfferRequest request) {
        return serverCaller.callServerWith(request);
    }
}
