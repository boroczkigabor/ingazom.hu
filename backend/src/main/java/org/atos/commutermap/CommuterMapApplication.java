package org.atos.commutermap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource(
        value = "/var/app/override.properties",
        ignoreResourceNotFound = true
)
@SpringBootApplication
public class CommuterMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuterMapApplication.class, args);
    }
}
