package com.hcl.physician_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhysicianPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhysicianPortalApplication.class, args);
    }

}