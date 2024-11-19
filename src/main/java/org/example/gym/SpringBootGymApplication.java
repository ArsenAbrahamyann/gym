package org.example.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class SpringBootGymApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGymApplication.class, args);
    }

}
