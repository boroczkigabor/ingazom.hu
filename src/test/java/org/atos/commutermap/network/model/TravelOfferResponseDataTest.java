package org.atos.commutermap.network.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TravelOfferResponseDataTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void deserializeTestResponse() throws IOException {
        TravelOfferResponse travelOffer = mapper.readerFor(TravelOfferResponse.class).readValue(new File("src/test/resources/network/testResponse.json"));

        Assertions.assertThat(travelOffer).isNotNull();
        Assertions.assertThat(travelOffer.travelOffers).isNotEmpty();
        travelOffer.travelOffers.forEach(System.out::println);
    }
}