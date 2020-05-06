package org.atos.commutermap.users.dao;

import org.atos.commutermap.users.config.UserServiceConfig;
import org.atos.commutermap.users.model.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        UserServiceConfig.class
})
@ActiveProfiles("sqlite")
@TestPropertySource(locations = "classpath:application.properties")
class ApplicationUserRepositoryIntegrationTest {

    @Autowired
    private ApplicationUserRepository repository;

    @Test
    void canFindAnExistingUser() {
        Optional<ApplicationUser> joe = repository.findById(1L);

        assertThat(joe).isNotEmpty();
        assertThat(joe.get().readAccessTokens()).isNotEmpty();
        assertThat(joe.get().email).isEqualTo("joe@foobar.com");
    }

    @Test
    void canRetrieveAllExistingUsers() {
        Iterable<ApplicationUser> users = repository.findAll();

        assertThat(users)
                .isNotEmpty()
                .doesNotContainNull();
    }

    @Test
    void canPersistAUserWithToken() {
        ApplicationUser applicationUser = new ApplicationUser("email", "issuer1", "token1");
        applicationUser.addAccessToken("issuer2", "token2");
        ApplicationUser savedUser = repository.save(applicationUser);

        Optional<ApplicationUser> userFromRepository = repository.findById(savedUser.userId);
        assertThat(userFromRepository)
                .isNotEmpty();
        assertThat(userFromRepository.get().readAccessTokens())
                .hasSize(2);

    }
}