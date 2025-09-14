package com.java.volunteer.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Database connection manager using HikariCP for connection pooling
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static HikariDataSource dataSource;
    
    // Private constructor to prevent instantiation
    private DatabaseManager() {}
    
    /**
     * Gets the data source, initializing it if needed
     * @return the data source
     */
    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }
    
    /**
     * Closes the data source
     */
    public static synchronized void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Shutting down connection pool");
            dataSource.close();
        }
    }
    
    /**
     * Initializes the data source using properties from db.properties
     */
    private static void initializeDataSource() {
        logger.info("Initializing database connection pool");
        
        try {
            Properties props = loadDbProperties();
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName(props.getProperty("db.driver"));
            
            // Connection pool settings
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.maxPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.minIdle", "5")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("db.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("db.idleTimeout", "600000")));
            config.setPoolName("VolunteerAppPool");
            
            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    /**
     * Loads database properties from db.properties file
     * @return the properties
     */
    private static Properties loadDbProperties() throws IOException {
        logger.info("Loading database properties");
        Properties props = new Properties();
        
        try (InputStream is = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                logger.error("db.properties not found in classpath");
                throw new IOException("db.properties not found in classpath");
            }
            props.load(is);
        }
        
        validateRequiredProperties(props);
        return props;
    }
    
    /**
     * Validates that all required properties are present
     * @param props the properties to validate
     * @throws IOException if any required property is missing
     */
    private static void validateRequiredProperties(Properties props) throws IOException {
        String[] required = {"db.url", "db.user", "db.password", "db.driver"};
        
        for (String prop : required) {
            if (!props.containsKey(prop) || props.getProperty(prop).trim().isEmpty()) {
                logger.error("Required property {} not found in db.properties", prop);
                throw new IOException("Required property " + prop + " not found in db.properties");
            }
        }
    }
}