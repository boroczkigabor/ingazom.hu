package org.atos.commutermap.dao.config;

import org.atos.commutermap.dao.StationRepository;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
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
@Import(DataSourceAutoConfiguration.class)
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
}
