package org.atos.commutermap.dao.config;

import org.atos.commutermap.common.config.DatabaseConfig;
import org.atos.commutermap.dao.RouteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(DatabaseConfig.class)
@EnableJpaRepositories(basePackageClasses = RouteRepository.class)
public class RouteDaoConfig {
}

