package org.atos.commutermap.users.config;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.atos.commutermap.users.network.service.TokenService;
import org.atos.commutermap.users.network.service.facebook.FacebookTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacebookAuthConfig {

    @Value("${oauth2.facebook.appID}")
    private String appID;
    @Value("${oauth2.facebook.appSecret}")
    private String appSecret;

    @Bean
    public TokenService facebookTokenService() {
        return new FacebookTokenService(facebookClient());
    }

    private FacebookClient facebookClient() {
        return new DefaultFacebookClient(Version.VERSION_6_0)
                .obtainAppAccessToken(appID, appSecret)
                .getClient();
    }
}
