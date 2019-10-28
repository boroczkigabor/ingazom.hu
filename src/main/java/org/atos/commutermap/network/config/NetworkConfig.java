package org.atos.commutermap.network.config;

import org.atos.commutermap.network.service.MavinfoServerCaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NetworkConfig {

    @Bean
    public MavinfoServerCaller mavinfoServerCaller() {
        return new MavinfoServerCaller(new RestTemplate());
    }
}
