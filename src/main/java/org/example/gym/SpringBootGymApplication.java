package org.example.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
@EnableDiscoveryClient
public class SpringBootGymApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGymApplication.class, args);
    }
}
