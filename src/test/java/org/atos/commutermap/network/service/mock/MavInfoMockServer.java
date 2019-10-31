package org.atos.commutermap.network.service.mock;

import org.apache.commons.io.FileUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.StringBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class MavInfoMockServer {
    private static final Random random = new Random();

    private final ClientAndServer mockServer;

    public MavInfoMockServer() {
        this.mockServer = startClientAndServer(1080);
    }

    public static void main(String[] args) {
        new MavInfoMockServer().init();
    }

    private void init() {
        new MockServerClient("localhost", 1080)
                .when(HttpRequest.request()
                        .withPath("/GetUtazasiAjanlat")
                        .withMethod("POST")
                        )
                .respond(HttpResponse.response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatusCode(200)
                        .withBody(new StringBody(readFile()))
                );
    }

    private String readFile() {
        try {
            String minute = String.valueOf(random.nextInt(60));
            return FileUtils.readFileToString(new File("src/test/resources/network/testResponse.json"))
                    .replace("00:23", "00:" + minute);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
