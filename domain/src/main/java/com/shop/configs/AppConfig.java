package com.shop.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    DatabaseConfig.class,
    ModelConfig.class,
    RepositoryConfig.class,
    ServiceConfig.class,
    ValidatorConfig.class
})
public class AppConfig {
}
