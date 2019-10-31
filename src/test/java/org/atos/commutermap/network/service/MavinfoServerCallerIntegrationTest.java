package org.atos.commutermap.network.service;

import org.assertj.core.api.Assertions;
import org.atos.commutermap.network.config.NetworkConfig;
import org.atos.commutermap.network.model.TestData;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = {
        NetworkConfig.class
})
@TestPropertySource( properties = {
        "mav.server.baseurl=http://localhost:1080"
})
class MavinfoServerCallerIntegrationTest {

    @Autowired
    private MavinfoServerCaller caller;

    @Test
    void testWithDummyRequest() {
        TravelOfferResponse response = caller.callServerWith(TestData.createTravelOfferRequest());
        Assertions.assertThat(response).isNotNull();
    }
}