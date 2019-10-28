package org.atos.commutermap.network.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class TravelOfferRequestJsonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void objectMapperShouldProduceProperRequest() throws IOException {
        TravelOfferRequest request = TestData.createTravelOfferRequest();
        String jsonString = objectMapper.writerFor(TravelOfferRequest.class)
                                        .withFeatures(SerializationFeature.INDENT_OUTPUT)
                                        .writeValueAsString(request);

        String expectedFileContent = FileUtils.readFileToString(new File("src/test/resources/network/testRequest.json"));
        Assertions.assertThat(jsonString).isEqualToIgnoringWhitespace(expectedFileContent);
    }

}