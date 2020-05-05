package org.atos.commutermap.users.network.service.facebook;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookGraphException;
import com.restfb.exception.FacebookOAuthException;
import org.atos.commutermap.users.model.ApplicationUser;
import org.atos.commutermap.users.network.service.TokenService;
import org.atos.commutermap.users.network.service.UserNotFoundException;
import org.atos.commutermap.users.network.service.UserPermissionMissingException;
import org.atos.commutermap.users.network.service.facebook.FacebookTokenService.FbApplicationUserHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacebookTokenServiceTest {
    private static final String AUTH_TOKEN = "kjdhfkdshfdfjsdkhfsiduf";

    @Mock
    private FacebookClient facebookClient;
    @Mock
    private FacebookClient.DebugTokenInfo debugTokenInfo;
    private TokenService service;

    @BeforeEach
    void setUp() {
        service = new FacebookTokenService(facebookClient);
    }

    @Test
    void retrieveWhenDebugFails_MustThrowUserNotFoundException() {
        when(facebookClient.debugToken(AUTH_TOKEN)).thenThrow(createFbException(FacebookGraphException.class));

        assertThrows(UserNotFoundException.class, () -> service.retrieveUserDetailsWithToken(AUTH_TOKEN));
    }

    @Test
    void retrieveWhenTokenIsNotValid_MustThrowUserNotFoundException() {
        when(facebookClient.debugToken(AUTH_TOKEN)).thenReturn(debugTokenInfo);
        when(debugTokenInfo.isValid()).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> service.retrieveUserDetailsWithToken(AUTH_TOKEN));
    }

    @Test
    void retrieveWhenEmailFetchingFails_MustThrowMissingPermissionException() {
        when(facebookClient.debugToken(AUTH_TOKEN)).thenReturn(debugTokenInfo);
        when(debugTokenInfo.isValid()).thenReturn(true);
        when(facebookClient.createClientWithAccessToken(AUTH_TOKEN)).thenAnswer(Answers.RETURNS_SELF);
        when(facebookClient.fetchObject(eq("me"), eq(FbApplicationUserHolder.class),Mockito.any(Parameter.class)))
                .thenThrow(createFbException(FacebookOAuthException.class));

        assertThrows(UserPermissionMissingException.class, () -> service.retrieveUserDetailsWithToken(AUTH_TOKEN));
    }

    @Test
    void retrieveWhenEmailIsNotReturnedInResponse_MustThrowMissingPermissionException() {
        when(facebookClient.debugToken(AUTH_TOKEN)).thenReturn(debugTokenInfo);
        when(debugTokenInfo.isValid()).thenReturn(true);
        when(facebookClient.createClientWithAccessToken(AUTH_TOKEN)).thenAnswer(Answers.RETURNS_SELF);
        when(facebookClient.fetchObject(eq("me"), eq(FbApplicationUserHolder.class),Mockito.any(Parameter.class)))
                .thenReturn(new FbApplicationUserHolder());

        assertThrows(UserPermissionMissingException.class, () -> service.retrieveUserDetailsWithToken(AUTH_TOKEN));
    }

    @Test
    void retrieveWhenAllDataIsReturnedInResponse_MustReturnAppUserObject() {
        when(facebookClient.debugToken(AUTH_TOKEN)).thenReturn(debugTokenInfo);
        when(debugTokenInfo.isValid()).thenReturn(true);
        when(facebookClient.createClientWithAccessToken(AUTH_TOKEN)).thenAnswer(Answers.RETURNS_SELF);
        when(facebookClient.fetchObject(eq("me"), eq(FbApplicationUserHolder.class),Mockito.any(Parameter.class)))
                .thenReturn(createFullHolderObject());

        ApplicationUser applicationUser = service.retrieveUserDetailsWithToken(AUTH_TOKEN);
        assertThat(applicationUser).isNotNull();
        assertThat(applicationUser.email).isEqualTo("joe@foobar.com");
        assertThat(applicationUser.accessTokens).containsEntry("Facebook", AUTH_TOKEN);
    }

    private FbApplicationUserHolder createFullHolderObject() {
        FbApplicationUserHolder fbApplicationUserHolder = new FbApplicationUserHolder();
        ReflectionTestUtils.setField(fbApplicationUserHolder, "email", "joe@foobar.com");
        ReflectionTestUtils.setField(fbApplicationUserHolder, "id", "123465789");
        return fbApplicationUserHolder;
    }

    @SuppressWarnings("unchecked")
    private <T extends FacebookGraphException> FacebookGraphException createFbException(Class<T> exceptionClass) {
        try {
            return (T) exceptionClass.getDeclaredConstructors()[0].newInstance("boom", "boom", 333, 3, 400, "", "", false, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}