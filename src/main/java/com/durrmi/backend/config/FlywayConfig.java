package com.durrmi.backend.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT to_regclass('public.pages') IS NOT NULL AS pages_exist, to_regclass('public.flyway_schema_history') IS NOT NULL AS history_exist;");
            if (rs.next()) {
                boolean pagesExist = rs.getBoolean("pages_exist");
                boolean historyExist = rs.getBoolean("history_exist");
                if (historyExist && !pagesExist) {
                    stmt.execute("DROP TABLE IF EXISTS flyway_schema_history;");
                }
            }
        } catch (Exception ignored) {
        }

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .load();
        return flyway;
    }
}
