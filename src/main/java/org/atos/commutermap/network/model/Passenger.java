package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.List;

public class Passenger {

    @JsonProperty(FieldNames.ONLY_WITH_EXTRA_PAYMENT)
    public final boolean onlyWithExtraPaymeng = false;
    @JsonProperty(FieldNames.DISABLED_PERSON)
    public final boolean disabledPerson = false;
    @JsonProperty(FieldNames.DISCOUNTS)
    public final List<String> discounts = Collections.emptyList();
    @JsonProperty(FieldNames.NUMBER_OF_TICKETS)
    public final int numberOfTickets = 1;
    @JsonProperty(FieldNames.PASSENGER_TYPE)
    public final PassengerType passengerType = PassengerType.ADULT;
    @JsonProperty("f")
    public final boolean f = false;

    enum PassengerType {
        ADULT("HU_44_026-065");

        @JsonValue
        final String stringValue;

        PassengerType(String stringValue) {

            this.stringValue = stringValue;
        }
    }
}