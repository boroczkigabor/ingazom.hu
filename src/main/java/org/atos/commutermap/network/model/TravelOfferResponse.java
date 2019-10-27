package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@JsonIgnoreProperties(value = {
        "Ajanlatok",
        "AjanlatHashKulcs"
})
public class TravelOfferResponse {

    public final List<TravelOffer> travelOffers;

    @JsonCreator
    public TravelOfferResponse(
            @JsonProperty("UtazasiAjanlatok") List<TravelOffer> travelOffers) {
        this.travelOffers = travelOffers;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
