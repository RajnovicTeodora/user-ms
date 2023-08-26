package com.notbooking.userms;

import com.notbooking.userms.dto.NewUserDTO;
import com.notbooking.userms.model.Host;
import com.notbooking.userms.model.User;
import com.notbooking.userms.repository.UserRepository;
import com.notbooking.userms.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(classes = UserMsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:tc:postgresql:11-alpine:///notBookingDBTest" })
@ActiveProfiles("test")
public class UserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTest.class);

    @Container
    private static final PostgreSQLContainer POSTGRES_SQL_CONTAINER;

    static {

        POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:11.1")
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("notBookingDBTest")
                .withPrivilegedMode(true);

        POSTGRES_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideTestProperties(DynamicPropertyRegistry registry) {

        System.out.println(POSTGRES_SQL_CONTAINER.getJdbcUrl());
        LOGGER.info("JDBC URL: {}", POSTGRES_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:11-alpine:///notBookingDBTest?loggerLevel=OFF"); // POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("javax.persistence.jdbc.url",
                () -> "jdbc:tc:postgresql:11-alpine:///notBookingDBTest?loggerLevel=OFF");
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");

        registry.add("integration-tests-db", POSTGRES_SQL_CONTAINER::getDatabaseName);

    }

    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testAddHost() {
        // Mocking repository behavior
        NewUserDTO newUserDTO = new NewUserDTO("hostUsername", "password", "name", "surname", "email", "HOST", "country", "city", "street", 2);
        Assertions.assertEquals("hostUsername", userService.addUser(newUserDTO));
    }
}