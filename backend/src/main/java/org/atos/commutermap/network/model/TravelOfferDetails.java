package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.atos.commutermap.dao.model.Station;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TravelOfferDetails {

    public final Station realDepartureStation;
    public final Station destinationStation;

    @JsonCreator
    public TravelOfferDetails(
            @JsonProperty(FieldNames.DEPARTURE_STATION) Station realDepartureStation,
            @JsonProperty(FieldNames.DESTINATION_STATION_RESPONSE) Station destinationStation) {
        this.realDepartureStation = realDepartureStation;
        this.destinationStation = destinationStation;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
