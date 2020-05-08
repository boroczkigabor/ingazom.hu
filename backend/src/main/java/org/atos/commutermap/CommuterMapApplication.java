package org.atos.commutermap;

import org.springframework.beans.factory.annotation.Value;
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
    public WebMvcConfigurer corsSupport(@Value("${http.cors.allowed.origins}") String allowedOrigins) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOrigins(allowedOrigins.split(";"))
                        .allowedHeaders("content-type", "Authorization", "Authorization-provider", "X-XSRF-TOKEN")
                        .allowedMethods("GET", "POST");
            }
        };
    }
}
