package org.atos.commutermap.users.network.service;

import org.atos.commutermap.users.network.service.facebook.FacebookTokenService;
import org.atos.commutermap.users.network.service.google.GoogleTokenService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class TokenServiceFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public TokenService getTokenServiceFor(@Nonnull String authProvider) {
        switch (authProvider.toLowerCase()) {
            case "google":
                return applicationContext.getBean(GoogleTokenService.class);
            case "facebook":
                return applicationContext.getBean(FacebookTokenService.class);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
