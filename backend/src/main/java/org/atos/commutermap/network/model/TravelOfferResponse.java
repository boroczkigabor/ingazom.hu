package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.atos.commutermap.common.model.BaseClass;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(value = {
        "Ajanlatok",
        "AjanlatHashKulcs"
})
public class TravelOfferResponse extends BaseClass {

    public final List<TravelOffer> travelOffers;
    public final List<ErrorMessage> errorMessages;

    @JsonCreator
    public TravelOfferResponse(
            @JsonProperty(FieldNames.TRAVEL_OFFERS) List<TravelOffer> travelOffers,
            @JsonProperty(FieldNames.MESSAGES) List<ErrorMessage> errorMessages) {
        this.travelOffers = travelOffers == null ? Collections.emptyList() : travelOffers;
        this.errorMessages = errorMessages == null ? Collections.emptyList() : errorMessages;
    }

}
