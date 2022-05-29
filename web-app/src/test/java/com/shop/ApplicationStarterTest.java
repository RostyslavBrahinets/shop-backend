package com.shop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ApplicationStarterTest {
    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
        });
    }
}
