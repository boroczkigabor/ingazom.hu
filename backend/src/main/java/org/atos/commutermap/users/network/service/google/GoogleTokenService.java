package org.atos.commutermap.users.network.service.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.apache.commons.lang3.StringUtils;
import org.atos.commutermap.users.model.ApplicationUser;
import org.atos.commutermap.users.network.service.TokenService;
import org.atos.commutermap.users.network.service.UserLookupException;
import org.atos.commutermap.users.network.service.UserNotFoundException;
import org.atos.commutermap.users.network.service.UserPermissionMissingException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public class GoogleTokenService implements TokenService {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleTokenService(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @Override
    public ApplicationUser retrieveUserDetailsWithToken(String authToken) throws UserLookupException {
        try {
            Optional<GoogleIdToken> verifiedToken = Optional.ofNullable(googleIdTokenVerifier.verify(authToken));
            return verifiedToken.map(googleToken -> {
                validateToken(googleToken);
                return new ApplicationUser(googleToken.getPayload().getEmail(), "Google", authToken);
            }).orElseThrow(() -> new UserNotFoundException("User not found using the provided authorization code"));
        } catch (GeneralSecurityException | IOException e) {
            throw new UserLookupException("Error occurred during verification of the token", e);
        }
    }

    private void validateToken(GoogleIdToken verifiedToken) throws UserNotFoundException, UserPermissionMissingException {
        if (StringUtils.isBlank(verifiedToken.getPayload().getEmail())) {
            throw new UserPermissionMissingException("User didn't provide permission to access the e-mail address");
        }
    }
}
