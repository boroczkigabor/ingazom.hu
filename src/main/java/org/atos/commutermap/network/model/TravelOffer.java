package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@JsonIgnoreProperties( value = {
        "IndDatumDT",
        "ErkDatumDT",
        "Szin",
        "Reszletek",
        "UtazasiAjanlatCsomagok",
        "AjanlatiCsomagGyujto",
        "Potjegyek",
})
public class TravelOffer {
    public final LocalDateTime departureDate;
    public final LocalDateTime arrivalDate;
    public final String travelTime;
    public final int distance;
    public final int price;
    public final boolean available;

    @JsonCreator
    public TravelOffer(
            @JsonProperty("IndDatum") long departureDate,
            @JsonProperty("ErkDatum") long arrivalDate,
            @JsonProperty("Idotartam") String travelTime,
            @JsonProperty("Km") int distance,
            @JsonProperty("Ar") int price,
            @JsonProperty("JegyAdhato") boolean available) {
        this.departureDate = LocalDateTime.ofEpochSecond(departureDate, 0, ZoneOffset.UTC);
        this.arrivalDate = LocalDateTime.ofEpochSecond(arrivalDate, 0, ZoneOffset.UTC);
        this.travelTime = travelTime;
        this.distance = distance;
        this.price = price;
        this.available = available;
    }
    /*
    "IndDatum": 1571912400,
      "IndDatumDT": "\/Date(1571912400000+0200)\/",
      "ErkDatum": 1571914020,
      "ErkDatumDT": "\/Date(1571914020000+0200)\/",
      "Idotartam": "00:27",
      "Km": 23,
      "Ar": 465,
      "Szin": "#000000",
      "Reszletek": [
     */

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
