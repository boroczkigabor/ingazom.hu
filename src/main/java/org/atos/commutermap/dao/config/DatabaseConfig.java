package org.atos.commutermap.dao.config;

import org.atos.commutermap.dao.StationRepository;
import org.hibernate.dialect.SQLiteDialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.sqlite.JDBC;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@EnableJpaRepositories(basePackageClasses = StationRepository.class)
@EnableTransactionManagement
@Configuration
public class DatabaseConfig {

    @Value("${database.driver.url}")
    private String dbUrl;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public JdbcTemplate jdbcTemplate() throws IOException {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC.class.getName());
        dataSource.setUrl(dbUrl);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws IOException {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setJpaDialect(new HibernateJpaDialect());
        entityManager.setDataSource(dataSource());
        entityManager.setPersistenceProvider(persistenceProvider());
        entityManager.setPackagesToScan(StationRepository.class.getPackage().getName());
        entityManager.setJpaProperties(additionalProperties());
        return entityManager;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", SQLiteDialect.class.getName());
        properties.setProperty("hibernate.show_sql", String.valueOf(true));

        return properties;
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
