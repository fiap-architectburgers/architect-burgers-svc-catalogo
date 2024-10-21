package com.example.gomesrodris.archburgers.adapters.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.VisibleForTesting;
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
    private final ComboPooledDataSource cpds;

    public DatabaseConnection(String driverClass, String dbUrl, String dbUser, String dbPass) {
        cpds = buildDataSource(driverClass, dbUrl, dbUser, dbPass);
    }

    @Autowired
    public DatabaseConnection(Environment environment) {
        String driverClassEnv = environment.getProperty("archburgers.datasource.driverClass");
        String dbUrlEnv = environment.getProperty("archburgers.datasource.dbUrl");
        String dbUserEnv = environment.getProperty("archburgers.datasource.dbUser");
        String dbPassEnv = environment.getProperty("archburgers.datasource.dbPass");

        if (driverClassEnv == null) {
            throw new IllegalStateException("driverClass env is missing");
        }
        if (dbUrlEnv == null) {
            throw new IllegalStateException("dbUrl env is missing");
        }
        if (dbUserEnv == null) {
            throw new IllegalStateException("dbUser env is missing");
        }
        if (dbPassEnv == null) {
            throw new IllegalStateException("dbPass env is missing");
        }

        cpds = buildDataSource(driverClassEnv, dbUrlEnv, dbUserEnv, dbPassEnv);
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

    private ComboPooledDataSource buildDataSource(String driverClass, String dbUrl, String dbUser, String dbPass) {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(driverClass);
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

        @VisibleForTesting
        public boolean isClosed() {
            try {
                return conn.isClosed();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
