package org.atos.commutermap.users.network.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.atos.commutermap.users.model.ApplicationUser;
import org.atos.commutermap.users.network.service.google.GoogleTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleTokenServiceTest {

    private static final String AUTH_CODE="dummy.code.here";

    @Mock
    private GoogleIdTokenVerifier tokenVerifier;
    @Mock
    private GoogleIdToken googleIdToken;
    @Mock
    private GoogleIdToken.Payload payload;

    private TokenService service;

    @BeforeEach
    void setUp() {
        service = new GoogleTokenService(tokenVerifier);
    }

    @Test
    void retrieveWhenVerifierThrowsSecurityException_MustThrowUserLookupException() throws GeneralSecurityException, IOException {
        when(tokenVerifier.verify(AUTH_CODE))
                .thenThrow(new GeneralSecurityException("boom"));

        assertThrows(UserLookupException.class, () -> service.retrieveUserDetailsWithToken(AUTH_CODE));
    }

    @Test
    void retrieveWhenVerifierThrowsIOException_MustThrowUserLookupException() throws GeneralSecurityException, IOException {
        when(tokenVerifier.verify(AUTH_CODE))
                .thenThrow(new IOException("boom"));

        assertThrows(UserLookupException.class, () -> service.retrieveUserDetailsWithToken(AUTH_CODE));
    }

    @Test
    void retrieveWhenVerifierReturnsNullObject_MustThrowUserLookupException() throws GeneralSecurityException, IOException {
        when(tokenVerifier.verify(AUTH_CODE))
                .thenReturn(null);

        assertThrows(UserLookupException.class, () -> service.retrieveUserDetailsWithToken(AUTH_CODE));
    }

    @Test
    void retrieveWhenUserIsReturned_EmailIsMissing_MustThrowUserPermissionMissingException() throws GeneralSecurityException, IOException {
        when(tokenVerifier.verify(AUTH_CODE)).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmail()).thenReturn(null);

        assertThrows(UserPermissionMissingException.class, () -> service.retrieveUserDetailsWithToken(AUTH_CODE));
    }

    @Test
    void retrieveWhenUserIsReturned_WithEmail_MustReturnApplicationUserObject() throws Exception {
        when(tokenVerifier.verify(AUTH_CODE)).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmail()).thenReturn("joe@foobar.com");

        ApplicationUser applicationUser = service.retrieveUserDetailsWithToken(AUTH_CODE);

        assertThat(applicationUser).isNotNull();
        assertThat(applicationUser.email).isEqualTo("joe@foobar.com");
    }
}