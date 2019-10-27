package org.atos.commutermap.network.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.atos.commutermap.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

class TravelOfferRequestJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void objectMapperShouldProduceProperRequest() throws IOException {
        TravelOfferRequest request = new TravelOfferRequest(
                new Station("005510009" , "Maglod"),
                LocalDateTime.ofEpochSecond(1571911800L, 0, ZoneOffset.UTC),
                new Station("005511551", "BUDAPEST*"),
                Collections.singletonList(new Passenger())
        );
        String jsonString = objectMapper.writerFor(TravelOfferRequest.class)
                                        .withFeatures(SerializationFeature.INDENT_OUTPUT)
                                        .writeValueAsString(request);

        String expectedFileContent = FileUtils.readFileToString(new File("src/test/resources/network/testRequest.json"));
        Assertions.assertThat(jsonString).isEqualToIgnoringWhitespace(expectedFileContent);
    }
}