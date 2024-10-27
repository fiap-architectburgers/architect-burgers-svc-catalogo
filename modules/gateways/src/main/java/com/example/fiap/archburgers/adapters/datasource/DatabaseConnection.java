package com.example.fiap.archburgers.adapters.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@Scope("singleton")
public class DatabaseConnection implements AutoCloseable {
    private static final String DRIVER_CLASS = org.postgresql.Driver.class.getName();

    private final ComboPooledDataSource cpds;

    public DatabaseConnection(String dbUrl, String dbUser, String dbPass) {
        cpds = buildDataSource(dbUrl, dbUser, dbPass);
    }

    @Autowired
    public DatabaseConnection(Environment environment) {
        String dbUrlEnv = environment.getProperty("archburgers.datasource.dbUrl");
        String dbUserEnv = environment.getProperty("archburgers.datasource.dbUser");
        String dbPassEnv = environment.getProperty("archburgers.datasource.dbPass");

        if (dbUrlEnv == null) {
            throw new IllegalStateException("archburgers.datasource.dbUrl env is missing");
        }
        if (dbUserEnv == null) {
            throw new IllegalStateException("archburgers.datasource.dbUser env is missing");
        }
        if (dbPassEnv == null) {
            throw new IllegalStateException("archburgers.datasource.dbPass env is missing");
        }

        cpds = buildDataSource(dbUrlEnv, dbUserEnv, dbPassEnv);
    }

    public ConnectionInstance getConnection() {
        try {
            return new ConnectionInstance(cpds.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException("Could not get DB connection: " + e.getMessage(), e);
        }
    }

    /**
     * Para uso em casos especiais como execução de Migration. Não utilize o acesso direto
     * nos métodos da aplicação!
     */
    public Connection jdbcConnection() throws SQLException {
        return cpds.getConnection();
    }

    private ComboPooledDataSource buildDataSource(String dbUrl, String dbUser, String dbPass) {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(DRIVER_CLASS);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        cpds.setJdbcUrl(dbUrl);
        cpds.setUser(dbUser);
        cpds.setPassword(dbPass);

        cpds.setMinPoolSize(1);
        cpds.setMaxPoolSize(10);

        return cpds;
    }

    @Override
    public void close() {
        cpds.close();
    }

    public static class ConnectionInstance implements AutoCloseable {
        private final Connection conn;

        public ConnectionInstance(Connection conn) {
            this.conn = conn;
        }

        public PreparedStatement prepareStatement(@Language("SQL") String sql) throws SQLException {
            return conn.prepareStatement(sql);
        }

        @Override
        public void close() {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing the connection: " + e, e);
            }
        }
    }
}
