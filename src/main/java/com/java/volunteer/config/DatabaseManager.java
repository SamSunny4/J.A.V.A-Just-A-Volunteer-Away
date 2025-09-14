package com.java.volunteer.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Database connection manager using HikariCP for connection pooling
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static HikariDataSource dataSource;
    
    static {
        try {
            initialize();
        } catch (IOException e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Could not initialize database connection", e);
        }
    }
    
    private static void initialize() throws IOException {
        logger.info("Initializing database connection pool");
        
        Properties props = new Properties();
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("config/database.properties")) {
            if (input == null) {
                throw new IOException("Unable to find database.properties");
            }
            props.load(input);
        }
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.username"));
        config.setPassword(props.getProperty("db.password"));
        config.setDriverClassName(props.getProperty("db.driver"));
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.connectionPoolSize", "10")));
        config.setAutoCommit(true);
        
        // Additional configurations
        config.setConnectionTimeout(30000); // 30s
        config.setIdleTimeout(600000); // 10m
        config.setMaxLifetime(1800000); // 30m
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        dataSource = new HikariDataSource(config);
        
        logger.info("Database connection pool initialized successfully");
    }
    
    /**
     * Get a connection from the pool
     * @return a database connection
     * @throws SQLException if unable to get a connection
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            try {
                initialize();
            } catch (IOException e) {
                logger.error("Failed to re-initialize database connection pool", e);
                throw new SQLException("Could not initialize database connection", e);
            }
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close the connection pool
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Closing database connection pool");
            dataSource.close();
        }
    }
}