package com.shop;

import com.shop.configs.DatabaseConfig;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
        setDatabase();
    }

    private static void setDatabase() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            DatabaseConfig.class
        );

        Flyway flyway = (Flyway) context.getBean("flyway");
        flyway.migrate();
    }
}
