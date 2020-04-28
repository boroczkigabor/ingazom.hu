package org.atos.commutermap.dao.config;

import org.atos.commutermap.dao.StationRepository;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;
import java.io.IOException;

@EnableJpaRepositories(basePackageClasses = StationRepository.class)
@EnableTransactionManagement
@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public JdbcTemplate jdbcTemplate() throws IOException {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws IOException {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setJpaDialect(new HibernateJpaDialect());
        entityManager.setDataSource(dataSource);
        entityManager.setPersistenceProvider(persistenceProvider());
        entityManager.setPackagesToScan(StationRepository.class.getPackage().getName());
        return entityManager;
    }

    @Bean
    public PersistenceProvider persistenceProvider() {
        return new HibernatePersistenceProvider();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Profile("!dev")
    @Configuration
    @Import(DataSourceAutoConfiguration.class)
    public static class NonDevDataSourceConfig {

    }

    @Profile("dev")
    @Configuration
    public static class DevDataSource {
        @Value("${spring.datasource.driver}")
        private String dataSourceDriver;

        @Value("${spring.datasource.url}")
        private String dataSourceUrl;

        @Value("${spring.datasource.username}")
        private String dataSourceUser;

        @Value("${spring.datasource.password}")
        private String dataSourcePassword;

        @Primary
        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(dataSourceDriver);
            dataSource.setUrl(dataSourceUrl);
            dataSource.setUsername(dataSourceUser);
            dataSource.setPassword(dataSourcePassword);
            return dataSource;
        }
    }
}
