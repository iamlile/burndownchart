package com.iamlile.jira.burndown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
/**
 * @author lee
 */
public class BurnDownServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BurnDownServerApplication.class, args);
    }
}
