package com.sparta.billy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BillyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillyApplication.class, args);
    }

}
