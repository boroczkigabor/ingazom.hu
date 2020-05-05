package org.atos.commutermap.users.network.service.facebook;

import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookGraphException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.atos.commutermap.users.model.ApplicationUser;
import org.atos.commutermap.users.network.service.TokenService;
import org.atos.commutermap.users.network.service.UserLookupException;
import org.atos.commutermap.users.network.service.UserNotFoundException;
import org.atos.commutermap.users.network.service.UserPermissionMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookTokenService implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookTokenService.class);

    private final FacebookClient facebookClient;

    public FacebookTokenService(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    public ApplicationUser retrieveUserDetailsWithToken(String authToken) throws UserLookupException {
        validateToken(authToken);
        FacebookClient fbClientForUser = facebookClient.createClientWithAccessToken(authToken);
        return retrieveUserDetailsUsing(authToken, fbClientForUser);
    }

    private ApplicationUser retrieveUserDetailsUsing(String authToken, FacebookClient fbClientForUser) {
        try {
            FbApplicationUserHolder data = fbClientForUser.fetchObject("me", FbApplicationUserHolder.class, Parameter.with("fields", "email"));
            if (!StringUtils.isBlank(data.email)) {
                return new ApplicationUser(data.email, "Facebook", authToken);
            }
        } catch (FacebookException e) {
            LOGGER.error("Failed to retrieve user e-mail due to exception", e);
        }
        throw new UserPermissionMissingException("Couldn't fetch e-mail data from Facebook");
    }

    private void validateToken(String authToken) {
        try {
            FacebookClient.DebugTokenInfo tokenInfo = facebookClient.debugToken(authToken);
            if (tokenInfo.isValid()) {
                return;
            }
        } catch (FacebookGraphException e) {
            LOGGER.warn("Unable to debug the incoming Facebook authToken due to exception: {}", ExceptionUtils.getMessage(e), e);
        }
        throw new UserNotFoundException("User not found based on the token");
    }

    static class FbApplicationUserHolder {
        @Facebook("email")
        private String email;
        @Facebook("id")
        private String id;
    }
}
