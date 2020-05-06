package org.atos.commutermap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@PropertySource(
        value = "file:/var/app/override.properties",
        ignoreResourceNotFound = true
)
@SpringBootApplication
public class CommuterMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuterMapApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsSupport() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("content-type", "Authorization", "Authorization-provider")
                        .allowedMethods("GET", "POST");
            }
        };
    }
}
