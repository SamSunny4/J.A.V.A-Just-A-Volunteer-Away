package com.java.volunteer.ui;

import java.util.Optional;

import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;
import com.java.volunteer.service.UserPointsService;
import com.java.volunteer.service.UserService;

/**
 * Login screen for user authentication
 */
public class LoginScreen extends BaseScreen {
    private final UserService userService;
    
    public LoginScreen(UserService userService) {
        super(null);
        this.userService = userService;
    }

    @Override
    public BaseScreen display() {
        showHeader("J.A.V.A - Just a Volunteer Away");
        
        int choice = showMenu(
            "Login",
            "Register",
            "Exit"
        );
        
        switch (choice) {
            case 1:
                return login();
            case 2:
                return register();
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private BaseScreen login() {
        System.out.println("\n=== Login ===");
        String username = getRequiredInput("Username");
        String password = getRequiredInput("Password");
        
        try {
            Optional<User> userOpt = userService.authenticate(username, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                currentUser = user;
                System.out.println("\nLogin successful. Welcome, " + user.getFirstName() + "!");
            } else {
                System.out.println("\nInvalid username or password. Please try again.");
            }
            
            if (currentUser != null) {
                // We don't have task service and user points service here, so we need to create or obtain them
                TaskService taskService = null;  // Ideally, this should be injected or created properly
                UserPointsService userPointsService = null;  // Ideally, this should be injected or created properly
                return new MainMenuScreen(currentUser, userService, taskService, userPointsService);
            }
        } catch (Exception e) {
            System.out.println("\nAn error occurred during login: " + e.getMessage());
            logger.error("Login error", e);
        }
        
        return this;
    }
    
    private BaseScreen register() {
        System.out.println("\n=== Register ===");
        
        User user = new User();
        user.setUsername(getRequiredInput("Username"));
        user.setPassword(getRequiredInput("Password"));
        user.setEmail(getRequiredInput("Email"));
        user.setFirstName(getRequiredInput("First Name"));
        user.setLastName(getRequiredInput("Last Name"));
        
        System.out.println("\nSelect User Role:");
        System.out.println("1. Elderly User");
        System.out.println("2. Volunteer");
        System.out.print("\nEnter your choice (1-2): ");
        
        int roleChoice;
        try {
            roleChoice = Integer.parseInt(scanner.nextLine().trim());
            if (roleChoice < 1 || roleChoice > 2) {
                System.out.println("Invalid choice. Please try again.");
                return this;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return this;
        }
        
        // Set role ID (2 = ELDERLY, 3 = VOLUNTEER)
        user.setRoleId(roleChoice + 1);
        
        try {
            user = userService.register(user);
            System.out.println("\nRegistration successful! You can now log in.");
        } catch (Exception e) {
            System.out.println("\nRegistration failed: " + e.getMessage());
            logger.error("Registration error", e);
        }
        
        return this;
    }
}