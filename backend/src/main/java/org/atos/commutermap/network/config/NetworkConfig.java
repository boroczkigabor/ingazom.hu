package org.atos.commutermap.network.config;

import org.apache.commons.io.IOUtils;
import org.atos.commutermap.network.service.ElviraUrlCreator;
import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.atos.commutermap.network.service.RestMavinfoServerCaller;
import org.atos.commutermap.network.service.RouteDurationColorizer;
import org.atos.commutermap.network.service.mock.MockMavinfoServerCaller;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
public class NetworkConfig {


    @Bean
    public RouteDurationColorizer routeDurationColorizer() {
        return new RouteDurationColorizer();
    }

    @Bean
    public ElviraUrlCreator elviraUrlCreator() {
        return new ElviraUrlCreator();
    }


    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public TomcatConnectorCustomizer utf8ConnectorCustomizer() {
        return connector -> connector.setURIEncoding(StandardCharsets.UTF_8.name());
    }

    @Configuration
    @Profile("prod")
    public static class ProductionNetworkConfig {

        @Value("${mav.server.baseurl}")
        private String serviceBaseUrl;

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
            return new RestMavinfoServerCaller(restOperations, serviceBaseUrl);
        }

    }

    @Configuration
    @Profile("!prod")
    public static class NonProductionNetworkConfig {

        @Bean
        public MavinfoServerCaller mavinfoServerCaller() {
            return new MockMavinfoServerCaller();
        }
    }

}
