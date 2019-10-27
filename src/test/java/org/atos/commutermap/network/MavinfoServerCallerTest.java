package org.atos.commutermap.network;

import org.apache.commons.io.FileUtils;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class MavinfoServerCallerTest {

    private MavinfoServerCaller caller = new MavinfoServerCaller();

    @Test
    void testWithDummyRequest() throws IOException {
        String fileContent = FileUtils.readFileToString(new File("src/test/resources/network/testRequest.json"));

        TravelOfferResponse response = caller.callServerWith(fileContent);
        System.out.println(response);
    }
}