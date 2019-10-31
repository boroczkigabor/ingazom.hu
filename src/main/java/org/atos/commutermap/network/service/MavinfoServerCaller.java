package org.atos.commutermap.network.service;

import org.atos.commutermap.network.model.TravelOfferRequest;
import org.atos.commutermap.network.model.TravelOfferResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestOperations;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class MavinfoServerCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(MavinfoServerCaller.class);
    private final RestOperations restOperations;
    private final String serviceBaseUrl;

    public MavinfoServerCaller(RestOperations restOperations, String serviceBaseUrl) {
        this.restOperations = restOperations;
        this.serviceBaseUrl = serviceBaseUrl;
        LOGGER.info("Initialized with url {}", serviceBaseUrl);
    }

    public TravelOfferResponse callServerWith(TravelOfferRequest payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(new MediaType("gzip")));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        headers.set("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10)");
        HttpEntity<TravelOfferRequest> requestBody = new HttpEntity<>(payload, headers);
        ResponseEntity<TravelOfferResponse> response = restOperations.exchange(serviceBaseUrl + "/GetUtazasiAjanlat",
                HttpMethod.POST, requestBody, TravelOfferResponse.class);
        LOGGER.info("Response arrived.");
        //LOGGER.info(response.toString());

        return response.getBody();
    }
}
