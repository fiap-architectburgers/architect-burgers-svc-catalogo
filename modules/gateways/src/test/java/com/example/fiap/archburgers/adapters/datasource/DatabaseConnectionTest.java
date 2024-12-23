package com.example.fiap.archburgers.adapters.datasource;//import static org.junit.jupiter.api.Assertions.*;

import com.example.fiap.archburgers.testUtils.StaticEnvironment;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;

class DatabaseConnectionTest {

    @Test
    void environmentVariablesValidation() {
        Map<String, String> env = new HashMap<>();

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.datasource.dbUrl env is missing");

        env.put("archburgers.datasource.dbUrl", "jdbc:postgresql://localhost:5432/archburgers");

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.datasource.dbUser env is missing");

        env.put("archburgers.datasource.dbUser", "burger");

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.datasource.dbPass env is missing");
    }
}