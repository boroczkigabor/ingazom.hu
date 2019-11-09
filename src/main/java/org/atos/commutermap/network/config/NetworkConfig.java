package org.atos.commutermap.network.config;

import org.apache.commons.io.IOUtils;
import org.atos.commutermap.network.service.ElviraUrlCreator;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.atos.commutermap.network.service.RouteDurationColorizer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Configuration
public class NetworkConfig {

    @Value("${mav.server.baseurl}")
    private String serviceBaseUrl;

    @Bean
    public RouteDurationColorizer routeDurationColorizer() {
        return new RouteDurationColorizer();
    }

    @Bean
    public ElviraUrlCreator elviraUrlCreator() {
        return new ElviraUrlCreator();
    }

    @Bean
    public MavinfoServerCaller mavinfoServerCaller() {
        RestTemplate restOperations = new RestTemplate();
        restOperations.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody, ClientHttpRequestExecution execution) throws IOException {

                LoggerFactory.getLogger("----->").info(new String(requestBody));

                ClientHttpResponse response = execution.execute(request, requestBody);
                byte[] responseBody = IOUtils.toByteArray(response.getBody());

                LoggerFactory.getLogger("<-----").info(new String(responseBody));

                return new ClientHttpResponse() {
                    @Override
                    public HttpStatus getStatusCode() throws IOException {
                        return response.getStatusCode();
                    }

                    @Override
                    public int getRawStatusCode() throws IOException {
                        return response.getRawStatusCode();
                    }

                    @Override
                    public String getStatusText() throws IOException {
                        return response.getStatusText();
                    }

                    @Override
                    public void close() {
                        response.close();
                    }

                    @Override
                    public InputStream getBody() throws IOException {
                        return new ByteArrayInputStream(responseBody);
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return response.getHeaders();
                    }
                };
            }
        }));
        return new MavinfoServerCaller(restOperations, serviceBaseUrl);
    }
}
