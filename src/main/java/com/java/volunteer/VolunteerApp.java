package com.java.volunteer;

import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;
import com.java.volunteer.service.UserPointsService;
import com.java.volunteer.service.UserService;
import com.java.volunteer.service.mock.MockTaskService;
import com.java.volunteer.service.mock.MockUserPointsService;
import com.java.volunteer.service.mock.MockUserService;
import com.java.volunteer.ui.BaseScreen;
import com.java.volunteer.ui.LoginScreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for the Volunteer Management System
 */
public class VolunteerApp {
    private static final Logger logger = LoggerFactory.getLogger(VolunteerApp.class);
    
    public static void main(String[] args) {
        logger.info("Starting Volunteer Management System");
        
        try {
            // Initialize Services with mock implementations for now
            // These can be replaced with real implementations later
            logger.info("Initializing services");
            MockUserService userService = new MockUserService();
            MockTaskService taskService = new MockTaskService();
            MockUserPointsService userPointsService = new MockUserPointsService();
            
            // Set up relationships between mock objects
            setupMockRelationships(userService, taskService, userPointsService);
            
            // Start the UI
            logger.info("Starting user interface");
            BaseScreen currentScreen = new LoginScreen(userService);
            
            while (currentScreen != null) {
                currentScreen = currentScreen.display();
            }
            
            logger.info("Application exiting normally");
        } catch (Exception e) {
            logger.error("Application encountered an error", e);
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Set up relationships between mock objects
     */
    private static void setupMockRelationships(MockUserService userService, MockTaskService taskService, 
                                              MockUserPointsService userPointsService) {
        logger.info("Setting up relationships between mock objects");
        
        // Set users for tasks
        taskService.getAllTasks().forEach(task -> {
            User requester = userService.getUserById(task.getRequesterId()).orElse(null);
            User volunteer = null;
            if (task.getVolunteerId() != null) {
                volunteer = userService.getUserById(task.getVolunteerId()).orElse(null);
            }
            taskService.setUserForTask(task, requester, volunteer);
        });
        
        // Set users for user points
        userPointsService.setUserForPoints(userPointsService.getLeaderboard(0), 
                                         userService.getAllUsers().stream()
                                             .collect(java.util.stream.Collectors.toMap(
                                                 User::getUserId, user -> user)));
    }
}