package com.oryanend.dicionario.filtro.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/status")
public class StatusController {
    private DataSource dataSource;

    public StatusController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    public Map<String, Object> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("updated_at", Instant.now().atZone(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_INSTANT));

        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put("database", getDatabaseInfo());

        response.put("dependencies", dependencies);
        return response;
    }

    private Map<String, Object> getDatabaseInfo() {
        Map<String, Object> dbInfo = new LinkedHashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            dbInfo.put("status", "healthy");
            dbInfo.put("version", meta.getDatabaseProductVersion().replaceAll("\\s*\\(.*?\\)", ""));
            dbInfo.put("max_connections", meta.getMaxConnections());

            if (dataSource instanceof com.zaxxer.hikari.HikariDataSource hikari) {
                dbInfo.put("opened_connections", hikari.getHikariPoolMXBean().getActiveConnections());
            } else {
                dbInfo.put("opened_connections", null);
            }

        } catch (Exception e) {
            dbInfo.put("status", "down");
            dbInfo.put("error", e.getMessage());
        }

        return dbInfo;
    }
}
