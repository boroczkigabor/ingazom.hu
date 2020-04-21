package org.atos.commutermap.network.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TravelOfferResponseDataTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void deserializeTestResponse() throws IOException {
        TravelOfferResponse travelOffer = mapper.readerFor(TravelOfferResponse.class).readValue(new File("src/test/resources/network/testResponse.json"));

        assertThat(travelOffer).isNotNull();
        assertThat(travelOffer.travelOffers).isNotEmpty();
        travelOffer.travelOffers.forEach(this::assertOfferDetailsNotNull);
        travelOffer.travelOffers.forEach(System.out::println);
    }

    private void assertOfferDetailsNotNull(TravelOffer travelOfferItem) {
        assertThat(travelOfferItem.details).isNotNull();
        assertThat(travelOfferItem.details.destinationStation).isNotNull();
        assertThat(travelOfferItem.details.realDepartureStation).isNotNull();
    }

    @Test
    void deserializeErrorResponse() throws IOException {
        TravelOfferResponse travelOffer = mapper.readerFor(TravelOfferResponse.class).readValue(new File("src/test/resources/network/errorResponse.json"));

        assertThat(travelOffer).isNotNull();
        assertThat(travelOffer.travelOffers).isEmpty();
        assertThat(travelOffer.errorMessages).hasSize(1);
        assertThat(travelOffer.errorMessages.get(0).text).isEqualTo("A feltételeknek megfelelő ajánlatot nem tudunk adni. Változtasson a feltételeken.");
    }

}