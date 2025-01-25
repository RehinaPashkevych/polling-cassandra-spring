package com.example.polling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.example.polling.repositories")
public class PollingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PollingApplication.class, args);
    }
}
