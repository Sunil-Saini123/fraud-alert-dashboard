package com.example.fraudalert.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.fraudalert.model.Role;
import com.example.fraudalert.service.UserService;

/**
 * Seeds initial users on first start-up if they don't already exist.
 * Credentials:  admin / admin123   (ADMIN role)
 *               analyst / analyst123  (ANALYST role)
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userService.usernameExists("admin")) {
            userService.createUser("admin", "admin123", Role.ADMIN);
            log.info("Created default admin user (username=admin, password=admin123)");
        }
        if (!userService.usernameExists("analyst")) {
            userService.createUser("analyst", "analyst123", Role.ANALYST);
            log.info("Created default analyst user (username=analyst, password=analyst123)");
        }
    }
}
