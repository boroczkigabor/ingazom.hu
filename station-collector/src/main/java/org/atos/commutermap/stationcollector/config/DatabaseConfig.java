package org.atos.commutermap.stationcollector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DatabaseConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public JdbcTemplate jdbcTemplate() throws IOException {
        return new JdbcTemplate(sqLiteDataSource());
    }

    @Bean
    public DataSource sqLiteDataSource() throws IOException {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite:" + resourceLoader.getResource("mav_vonat_info.db").getFile().getAbsolutePath());
        sqLiteDataSource.setDatabaseName("mav_vonat_info");
        sqLiteDataSource.setReadOnly(true);
        return sqLiteDataSource;
    }

}
