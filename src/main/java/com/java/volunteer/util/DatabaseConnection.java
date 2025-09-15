package com.java.volunteer.util;

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
 * Database connection utility using HikariCP connection pool
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;
    
    static {
        try {
            initialize();
        } catch (IOException e) {
            logger.error("Failed to initialize database connection", e);
        }
    }
    
    private static void initialize() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("config/database.properties")) {
            if (inputStream == null) {
                throw new IOException("Database properties file not found");
            }
            properties.load(inputStream);
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(properties.getProperty("db.driver"));
            config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.connectionPoolSize", "10")));
            
            // Additional HikariCP settings
            config.setAutoCommit(true);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setPoolName("VolunteerAppConnectionPool");
            
            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing database connection pool", e);
            throw e;
        }
    }
    
    /**
     * Get a connection from the pool
     * 
     * @return database connection
     * @throws SQLException if connection cannot be obtained
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            try {
                initialize();
            } catch (IOException e) {
                throw new SQLException("Failed to initialize database connection", e);
            }
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close the connection pool
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }
}