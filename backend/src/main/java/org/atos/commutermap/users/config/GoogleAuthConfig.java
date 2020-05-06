package org.atos.commutermap.users.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.atos.commutermap.users.network.service.google.GoogleTokenService;
import org.atos.commutermap.users.network.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleAuthConfig {

    @Value("${oauth2.google.clientID}")
    private String clientID;

    @Bean
    public TokenService googleTokenService() {
        return new GoogleTokenService(googleIdTokenVerifier());
    }

    @Bean
    GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientID))
                .build();
    }
}
