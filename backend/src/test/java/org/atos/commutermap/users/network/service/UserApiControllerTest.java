package org.atos.commutermap.users.network.service;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.users.dao.ApplicationUserRepository;
import org.atos.commutermap.users.model.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("sqlite")
@WebMvcTest(UserApiController.class)
@ExtendWith({
        SpringExtension.class,
        MockitoExtension.class
})
class UserApiControllerTest {

    private static final String AUTH_TOKEN = "asdfghjklertyuiop";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationUserRepository userRepository;
    @MockBean
    private TokenService tokenService;

    @Test
    void loginWhenUserDoesntExistMustReturnBadRequest() throws Exception {
        when(tokenService.retrieveUserDetailsWithToken(AUTH_TOKEN))
                .thenThrow(new UserNotFoundException("No user"));

        mockMvc.perform(loginRequest())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No user"));
    }

    @Test
    void loginWhenUserDidntProvidePermissionForEmail() throws Exception {
        when(tokenService.retrieveUserDetailsWithToken(AUTH_TOKEN))
                .thenThrow(new UserPermissionMissingException("No email"));

        mockMvc.perform(loginRequest())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No email"));
    }

    @Test
    void loginWhenUserLookupFails_MustReturnInternalServerError() throws Exception {
        when(tokenService.retrieveUserDetailsWithToken(AUTH_TOKEN))
                .thenThrow(new UserLookupException("No service"));

        mockMvc.perform(loginRequest())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("No service"));
    }

    @Test
    void loginWhenUserDoesntExistShouldCreate_Return201() throws Exception {
        ApplicationUser joe = new ApplicationUser("joe@foobar.com", ImmutableMap.of("provider", "token"));
        when(tokenService.retrieveUserDetailsWithToken(AUTH_TOKEN))
                .thenReturn(joe);
        when(userRepository.findByEmail("joe@foobar.com")).thenReturn(Optional.empty());
        when(userRepository.save(joe)).thenReturn(joe);

        mockMvc.perform(loginRequest())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/user/" + Long.MIN_VALUE)));

        Mockito.verify(userRepository).save(Mockito.eq(joe));
    }

    @Test
    void loginWhenUserExistsShouldUpdateTokens_Return200() throws Exception {
        ApplicationUser joe = new ApplicationUser("joe@foobar.com", ImmutableMap.of("provider", "token"));
        when(tokenService.retrieveUserDetailsWithToken(AUTH_TOKEN)).thenReturn(joe);
        ApplicationUser joeInTheSystemAlready = new ApplicationUser("joe@foobar.com", ImmutableMap.of("provider", "old_token"));
        when(userRepository.findByEmail("joe@foobar.com"))
                .thenReturn(Optional.of(joeInTheSystemAlready));
        when(userRepository.save(joeInTheSystemAlready)).thenReturn(joeInTheSystemAlready);

        mockMvc.perform(loginRequest())
                .andExpect(status().isOk());

        Mockito.verify(userRepository).save(Mockito.eq(joe));
    }

    private MockHttpServletRequestBuilder loginRequest() {
        return MockMvcRequestBuilders.post("/user/login")
                .header("Authorization", AUTH_TOKEN)
                .header("Authorization-provider", "provider");
    }

}