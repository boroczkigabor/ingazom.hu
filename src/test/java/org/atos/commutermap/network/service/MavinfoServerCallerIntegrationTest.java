package org.atos.commutermap.network.service;

import org.atos.commutermap.network.config.NetworkConfig;
import org.atos.commutermap.network.model.TestData;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = {
        NetworkConfig.class
})
@TestPropertySource( properties = {
        "mav.server.baseurl=http://vim.mav-start.hu/VIM/PR/20190610/MobileService.svc/rest/"
})
class MavinfoServerCallerIntegrationTest {

    @Autowired
    private MavinfoServerCaller caller;

    @Test
    void testWithDummyRequest() throws IOException {
        TravelOfferResponse response = caller.callServerWith(TestData.createTravelOfferRequest());
        assertThat(response).isNotNull();
    }

    @Test
    void testWithSame() throws IOException {
        TravelOfferResponse response = caller.callServerWith(TestData.defaultBuilder()
                .withDeparture(TestData.STATION_MAGLOD)
                .withDestination(TestData.STATION_MAGLOD)
                .build());
        assertThat(response).isNotNull();
        assertThat(response.travelOffers).isEmpty();
        assertThat(response.errorMessages).isNotEmpty();
    }

}