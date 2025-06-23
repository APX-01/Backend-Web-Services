package com.education.eduhive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EduhiveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduhiveBackendApplication.class, args);
    }

}
