package com.irostub.learnspringdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LearnSpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnSpringDataJpaApplication.class, args);
    }

}
