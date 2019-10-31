package org.atos.commutermap;

import org.atos.commutermap.batch.BatchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class CommuterMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuterMapApplication.class, args);
    }
}
