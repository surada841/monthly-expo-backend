package com.monthlyexpo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MonthlyExpoApplication {

    public static void main(String[] args) {
        log.info("Starting Monthly Expo backend application");
        SpringApplication.run(MonthlyExpoApplication.class, args);
        log.info("Monthly Expo backend application started successfully");
    }
}