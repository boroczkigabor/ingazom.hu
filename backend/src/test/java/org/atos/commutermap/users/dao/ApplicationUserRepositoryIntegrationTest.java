package org.atos.commutermap.users.dao;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.users.config.UserServiceConfig;
import org.atos.commutermap.users.model.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
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
        assertThat(joe.get().accessTokens).isNotEmpty();
        assertThat(joe.get().eMailAddress).isEqualTo("joe@foobar.com");
    }

    @Test
    void canRetrieveAllExistingUsers() {
        Iterable<ApplicationUser> users = repository.findAll();

        assertThat(users)
                .isNotEmpty()
                .doesNotContainNull();
    }

    @Transactional
    @Test
    void canPersistAUser() {
        ApplicationUser user = repository.save(new ApplicationUser("email", ImmutableMap.of()));

        Optional<ApplicationUser> userFromRepository = repository.findById(user.userId);
        assertThat(userFromRepository)
                .isNotEmpty();

    }

    @Transactional
    @Test
    void canPersistAUserWithTokens() {
        ApplicationUser user = repository.save(new ApplicationUser("email", ImmutableMap.of(
                "issuer1", "token1",
                "issuer2", "token2"
        )));

        Optional<ApplicationUser> userFromRepository = repository.findById(user.userId);
        assertThat(userFromRepository)
                .isNotEmpty();
        assertThat(userFromRepository.get().accessTokens)
                .hasSize(2);

    }
}