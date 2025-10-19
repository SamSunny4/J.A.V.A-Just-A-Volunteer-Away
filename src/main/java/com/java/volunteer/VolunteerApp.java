package com.java.volunteer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.ui.ConsoleUI;
import com.java.volunteer.util.DatabaseConnection;

/**
 * Main application class for the J.A.V.A (Just a Volunteer Away) application
 */
public class VolunteerApp {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerApp.class);

    public static void main(String[] args) {
        logger.info("Starting J.A.V.A (Just a Volunteer Away) application");
        
        try {
            // Test database connection to ensure it works
            DatabaseConnection.getConnection();
            logger.info("Database connection established successfully");
            
            // Start the console UI
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (Exception e) {
            logger.error("Application initialization error", e);
            System.out.println("An error occurred while starting the application:");
            System.out.println(e.getMessage());
            System.out.println("Please check the database connection and try again.");
        } finally {
            // Close the database connection pool when the application exits
            DatabaseConnection.closePool();
            logger.info("Application shutdown complete");
        }
    }
}