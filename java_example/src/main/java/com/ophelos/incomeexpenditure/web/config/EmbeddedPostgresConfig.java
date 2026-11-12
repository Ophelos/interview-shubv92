package com.ophelos.incomeexpenditure.web.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
public class EmbeddedPostgresConfig {

    @Bean(destroyMethod = "close")
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        return EmbeddedPostgres.builder()
                .setDataDirectory(new File("embedded-pg-data"))
                .setCleanDataDirectory(false) // Don't wipe data on startup
                .start();
    }

    @Bean
    public DataSource dataSource(EmbeddedPostgres pg) throws SQLException {
        return new SimpleDriverDataSource(
                new org.postgresql.Driver(),
                pg.getJdbcUrl("postgres", "postgres"), // Default username/password
                "postgres",
                "postgres"
        );
    }
}
