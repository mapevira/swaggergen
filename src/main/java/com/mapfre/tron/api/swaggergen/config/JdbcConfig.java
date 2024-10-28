package com.mapfre.tron.api.swaggergen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuration class for setting up JdbcTemplate with a data source in Spring.
 * <p>
 * This class provides a bean configuration for {@link JdbcTemplate}, enabling
 * it to perform database operations using a specified {@link DataSource}.
 * <p>
 *
 * @author architecture - rperezv
 * @version 25/10/2024 - 12:44
 * @since jdk 1.17
 */
@Configuration
public class JdbcConfig {

    /**
     * Defines the {@link JdbcTemplate} bean for performing SQL operations
     * using the given {@link DataSource}.
     *
     * @param dataSource the data source used for database connections
     * @return a configured instance of {@link JdbcTemplate}
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        // Creates and returns a JdbcTemplate with the specified DataSource
        return new JdbcTemplate(dataSource);
    }

}
