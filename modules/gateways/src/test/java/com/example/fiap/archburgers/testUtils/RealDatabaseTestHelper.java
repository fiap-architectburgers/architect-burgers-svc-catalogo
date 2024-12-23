package com.example.fiap.archburgers.testUtils;

import com.example.fiap.archburgers.adapters.datasource.DatabaseConnection;
import com.example.fiap.archburgers.tools.migration.DatabaseMigration;
import org.jetbrains.annotations.VisibleForTesting;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class RealDatabaseTestHelper {
    private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:12-alpine"
    );

    public void beforeAll() throws Exception {
        postgres.start();

        try (DatabaseMigration migration = new DatabaseMigration(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            migration.runMigrations();
        }
    }

    public void afterAll() {
        postgres.stop();
    }

    public DatabaseConnection getConnectionPool() {
        var env = new StaticEnvironment(Map.of(
                "archburgers.datasource.dbUrl", postgres.getJdbcUrl(),
                "archburgers.datasource.dbUser", postgres.getUsername(),
                "archburgers.datasource.dbPass", postgres.getPassword()
        ));

        return new DatabaseConnection(env);
    }

    @VisibleForTesting
    public String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    @VisibleForTesting
    public String getJdbcUsername() {
        return postgres.getUsername();
    }

    @VisibleForTesting
    public String getJdbcPassword() {
        return postgres.getPassword();
    }
}
