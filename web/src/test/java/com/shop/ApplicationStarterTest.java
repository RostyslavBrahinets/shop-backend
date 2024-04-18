package com.shop;

import com.shop.utilities.ImageDatabaseUtility;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@SpringBootTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    ApplicationStarterTest.TestContextConfig.class,
    ImageDatabaseUtility.class
})
class ApplicationStarterTest {
    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> {
        });
    }

    @TestConfiguration
    @PropertySource("classpath:database-test.properties")
    static class TestContextConfig {
        @Bean
        public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.driver-class-name}") String driverClassName
        ) {
            final HikariConfig config = new HikariConfig();

            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(url);

            return new HikariDataSource(config);
        }

        @Bean
        public Flyway flyway(DataSource dataSource) {
            return Flyway.configure()
                .outOfOrder(true)
                .locations("classpath:db/migration")
                .dataSource(dataSource)
                .load();
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }
    }
}
