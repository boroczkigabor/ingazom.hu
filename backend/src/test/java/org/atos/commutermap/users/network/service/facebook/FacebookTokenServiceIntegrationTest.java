package org.atos.commutermap.users.network.service.facebook;

import org.atos.commutermap.users.config.FacebookAuthConfig;
import org.atos.commutermap.users.network.service.TokenService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        FacebookAuthConfig.class
})
@ActiveProfiles("dev")
@TestPropertySource(locations = {
        "classpath:application.properties",
        "classpath:application-dev.properties"
})
class FacebookTokenServiceIntegrationTest {

    @Autowired
    private TokenService facebookTokenService;

//    @Test
    void retrieveValidatesToken() {
        facebookTokenService.retrieveUserDetailsWithToken("EAAJXmhUjAvcBAGKK3ssowpASZB3I4SeES4cVSeGITlFAzc8ht9i7ypmeZAFEXRkXmzL6VxxaYvCWqMdvzgQZCALq31P53B2M8QcU0HnfVhVgnpYBQFQ0XFhZCegJt5hNyPDr2HLdDMitjMqHt0qXFH475OxojYWAOFvxOSldwQNUr6ZBNqQjblLIfZAy6LFFsTa8gDeciDlaWUEeaWejEm");
    }
}