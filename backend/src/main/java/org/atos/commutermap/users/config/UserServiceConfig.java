package org.atos.commutermap.users.config;

import org.atos.commutermap.common.config.DatabaseConfig;
import org.atos.commutermap.users.dao.ApplicationUserRepository;
import org.atos.commutermap.users.model.ApplicationUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(DatabaseConfig.class)
@EnableJpaRepositories(basePackageClasses = {
        ApplicationUserRepository.class,
        ApplicationUser.class
})
public class UserServiceConfig {
}
