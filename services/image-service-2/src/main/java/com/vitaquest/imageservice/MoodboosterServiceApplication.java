package com.vitaquest.imageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class MoodboosterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoodboosterServiceApplication.class, args);
    }
}
