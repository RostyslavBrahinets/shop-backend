package com.shop.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {
    @Bean
    public DataSource dataSource(
        @Value("${database.url}") String url,
        @Value("${database.username}") String username,
        @Value("${database.password}") String password
    ) {
        final HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

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
