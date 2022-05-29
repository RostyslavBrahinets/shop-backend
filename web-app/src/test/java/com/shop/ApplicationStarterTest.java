package com.shop;

import com.shop.configs.DatabaseConfig;
import com.shop.utilities.ImageDatabaseUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {
    DatabaseConfig.class,
    ImageDatabaseUtility.class
})
public class ApplicationStarterTest {
    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> {
        });
    }
}
