package org.atos.commutermap.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Profile("!sqlite")
    @Configuration
    @Import(DataSourceAutoConfiguration.class)
    public static class NonDevDataSourceConfig {

    }

    @Profile("sqlite")
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
