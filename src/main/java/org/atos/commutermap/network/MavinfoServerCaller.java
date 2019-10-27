package org.atos.commutermap.network;

import org.atos.commutermap.network.model.TravelOfferResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.logging.Logger;

public class MavinfoServerCaller {

    private final RestOperations restOperations = new RestTemplate();

    public TravelOfferResponse callServerWith(String payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(new MediaType("gzip")));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        headers.set("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10)");
        HttpEntity<String> requestBody = new HttpEntity<>(payload, headers);
        ResponseEntity<TravelOfferResponse> response = restOperations.exchange("http://vim.mav-start.hu/VIM/PR/20190610/MobileService.svc/rest/GetUtazasiAjanlat",
                HttpMethod.POST, requestBody, TravelOfferResponse.class);
        Logger.getAnonymousLogger().info("Response arrived.");
        Logger.getAnonymousLogger().info(response.toString());

        return response.getBody();
    }
}
