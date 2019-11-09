package org.atos.commutermap.network.config;

import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.atos.commutermap.network.service.RouteDurationColorizer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NetworkConfig {

    @Value("${mav.server.baseurl}")
    private String serviceBaseUrl;

    @Bean
    public RouteDurationColorizer routeDurationColorizer() {
        return new RouteDurationColorizer();
    }

    @Bean
    public MavinfoServerCaller mavinfoServerCaller() {
        return new MavinfoServerCaller(new RestTemplate(), serviceBaseUrl);
    }
}
