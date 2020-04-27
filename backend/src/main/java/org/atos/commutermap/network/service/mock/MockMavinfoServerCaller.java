package org.atos.commutermap.network.service.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MockMavinfoServerCaller implements MavinfoServerCaller {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();

    public MockMavinfoServerCaller() {
        LoggerFactory.getLogger(getClass()).info("---- Loaded mocked MAV server caller ----");
    }

    @Override
    public TravelOfferResponse callServerWith(TravelOfferRequest payload) {
        try {
            int minute = random.nextInt(60);
            String responseJson = StreamUtils.copyToString(getClass().getResourceAsStream("/network/mock/mockResponse.json"), UTF_8)
                    .replaceAll("\"Idotartam\": \"00:27\"", String.format("\"Idotartam\": \"00:%02d\"", minute));

            return objectMapper.readerFor(TravelOfferResponse.class).readValue(responseJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
