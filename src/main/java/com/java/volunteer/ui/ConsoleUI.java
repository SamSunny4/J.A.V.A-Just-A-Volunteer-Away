package com.java.volunteer.ui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;
import com.java.volunteer.service.UserService;
import com.java.volunteer.service.impl.TaskServiceImpl;
import com.java.volunteer.service.impl.UserServiceImpl;

/**
 * Command-line interface for the Volunteer Application
 */
public class ConsoleUI {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleUI.class);
    private final Scanner scanner;
    private final UserService userService;
    private final TaskService taskService;
    private User currentUser;
    
    // Date and time formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserServiceImpl();
        this.taskService = new TaskServiceImpl();
    }
    
    /**
     * Start the application
     */
    public void start() {
        boolean running = true;
        
        while (running) {
            if (currentUser == null) {
                displayMainMenu();
                int choice = readIntInput("Enter your choice: ", 1, 4);
                
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        registerUser();
                        break;
                    case 3:
                        displayAboutInfo();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Exiting application. Goodbye!");
                        break;
                }
            } else {
                // User is logged in, display role-specific menu
                if (currentUser.getRole().equals("ELDERLY")) {
                    displayElderlyMenu();
                    handleElderlyMenuChoice();
                } else if (currentUser.getRole().equals("VOLUNTEER")) {
                    displayVolunteerMenu();
                    handleVolunteerMenuChoice();
                } else {
                    // Admin menu not implemented for this simple version
                    System.out.println("Admin functionality not yet implemented. Logging out.");
                    logout();
                }
            }
        }
    }
    
    /**
     * Display the main menu for non-authenticated users
     */
    private void displayMainMenu() {
        System.out.println("\n===== J.A.V.A (Just a Volunteer Away) =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. About");
        System.out.println("4. Exit");
    }
    
    /**
     * Display the menu for elderly users
     */
    private void displayElderlyMenu() {
        System.out.println("\n===== Elderly User Menu =====");
        System.out.println("Welcome, " + currentUser.getFirstName() + "!");
        System.out.println("1. Create a New Task Request");
        System.out.println("2. View My Tasks");
        System.out.println("3. Update My Profile");
        System.out.println("4. Logout");
    }
    
    /**
     * Display the menu for volunteer users
     */
    private void displayVolunteerMenu() {
        System.out.println("\n===== Volunteer Menu =====");
        System.out.println("Welcome, " + currentUser.getFirstName() + "!");
        System.out.println("1. View Available Tasks");
        System.out.println("2. View My Assigned Tasks");
        System.out.println("3. Update Task Status");
        System.out.println("4. Update My Profile");
        System.out.println("5. Logout");
    }
    
    /**
     * Handle elderly user menu choices
     */
    private void handleElderlyMenuChoice() {
        int choice = readIntInput("Enter your choice: ", 1, 4);
        
        switch (choice) {
            case 1:
                createTask();
                break;
            case 2:
                viewMyTasks();
                break;
            case 3:
                updateProfile();
                break;
            case 4:
                logout();
                break;
        }
    }
    
    /**
     * Handle volunteer user menu choices
     */
    private void handleVolunteerMenuChoice() {
        int choice = readIntInput("Enter your choice: ", 1, 5);
        
        switch (choice) {
            case 1:
                viewAvailableTasks();
                break;
            case 2:
                viewMyAssignedTasks();
                break;
            case 3:
                updateTaskStatus();
                break;
            case 4:
                updateProfile();
                break;
            case 5:
                logout();
                break;
        }
    }
    
    /**
     * Login a user
     */
    private void login() {
        System.out.println("\n===== Login =====");
        String username = readStringInput("Username: ");
        String password = readStringInput("Password: ");
        
        try {
            User user = userService.authenticateUser(username, password);
            
            if (user != null) {
                currentUser = user;
                logger.info("User logged in: {}", username);
                System.out.println("Login successful! Welcome, " + user.getFirstName() + "!");
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred during login: " + e.getMessage());
            logger.error("Login error", e);
        }
    }
    
    /**
     * Register a new user
     */
    private void registerUser() {
        System.out.println("\n===== Register New User =====");
        
        String username = readStringInput("Username: ");
        String password = readStringInput("Password (at least 8 characters with uppercase, lowercase, and a digit): ");
        String email = readStringInput("Email: ");
        String firstName = readStringInput("First Name: ");
        String lastName = readStringInput("Last Name: ");
        String phoneNumber = readStringInput("Phone Number: ");
        String address = readStringInput("Address: ");
        
        // Read date of birth
        LocalDate dateOfBirth = null;
        boolean validDate = false;
        while (!validDate) {
            try {
                String dateStr = readStringInput("Date of Birth (YYYY-MM-DD): ");
                dateOfBirth = LocalDate.parse(dateStr, DATE_FORMATTER);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        // Choose role
        System.out.println("\nSelect Role:");
        System.out.println("1. Elderly");
        System.out.println("2. Volunteer");
        int roleChoice = readIntInput("Enter your choice: ", 1, 2);
        String role = roleChoice == 1 ? "ELDERLY" : "VOLUNTEER";
        
        String bio = readStringInput("Bio (brief description about yourself): ");
        
        try {
            User newUser = userService.registerUser(username, password, email, firstName, lastName,
                    phoneNumber, address, dateOfBirth, role, bio);
            
            System.out.println("\nRegistration successful! You can now login with your credentials.");
            logger.info("New user registered: {}", username);
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
            logger.warn("Registration failed: {}", e.getMessage());
        } catch (SQLException e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
            logger.error("Registration error", e);
        }
    }
    
    /**
     * Create a new task (for elderly users)
     */
    private void createTask() {
        System.out.println("\n===== Create New Task =====");
        
        String title = readStringInput("Task Title: ");
        String description = readStringInput("Task Description: ");
        String location = readStringInput("Location: ");
        
        // Read scheduled date
        LocalDate scheduledDate = null;
        boolean validDate = false;
        while (!validDate) {
            try {
                String dateStr = readStringInput("Scheduled Date (YYYY-MM-DD): ");
                scheduledDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                if (scheduledDate.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in the past. Please enter a future date.");
                } else {
                    validDate = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        // Read scheduled time
        LocalTime scheduledTime = null;
        boolean validTime = false;
        while (!validTime) {
            try {
                String timeStr = readStringInput("Scheduled Time (HH:MM, 24-hour format): ");
                scheduledTime = LocalTime.parse(timeStr, TIME_FORMATTER);
                validTime = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:MM format.");
            }
        }
        
        int estimatedDuration = readIntInput("Estimated Duration (in minutes): ", 15, 480);
        
        // Choose urgency level
        System.out.println("\nSelect Urgency Level:");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
        int urgencyChoice = readIntInput("Enter your choice: ", 1, 3);
        String urgencyLevel = "";
        switch (urgencyChoice) {
            case 1:
                urgencyLevel = "LOW";
                break;
            case 2:
                urgencyLevel = "MEDIUM";
                break;
            case 3:
                urgencyLevel = "HIGH";
                break;
        }
        
        try {
            Task newTask = taskService.createTask(title, description, currentUser.getUserId(), 
                    location, scheduledDate, scheduledTime, estimatedDuration, urgencyLevel);
            
            System.out.println("\nTask created successfully! Task ID: " + newTask.getTaskId());
            logger.info("New task created: {} (ID: {})", title, newTask.getTaskId());
        } catch (IllegalArgumentException e) {
            System.out.println("Task creation failed: " + e.getMessage());
            logger.warn("Task creation failed: {}", e.getMessage());
        } catch (SQLException e) {
            System.out.println("An error occurred during task creation: " + e.getMessage());
            logger.error("Task creation error", e);
        }
    }
    
    /**
     * View tasks requested by the current elderly user
     */
    private void viewMyTasks() {
        System.out.println("\n===== My Tasks =====");
        
        try {
            List<Task> tasks = taskService.getTasksByRequesterId(currentUser.getUserId());
            
            if (tasks.isEmpty()) {
                System.out.println("You have no tasks.");
                return;
            }
            
            for (Task task : tasks) {
                System.out.println("ID: " + task.getTaskId() + 
                        " | Title: " + task.getTitle() +
                        " | Status: " + task.getStatus() +
                        " | Date: " + task.getScheduledDate() +
                        " | Time: " + task.getScheduledTime());
            }
            
            // Ask if the user wants to view details of a specific task
            System.out.println("\nEnter a task ID to view details or 0 to go back:");
            int taskId = readIntInput("Task ID: ", 0, Integer.MAX_VALUE);
            
            if (taskId > 0) {
                Task selectedTask = taskService.getTaskById(taskId);
                if (selectedTask != null && selectedTask.getRequesterId() == currentUser.getUserId()) {
                    displayTaskDetails(selectedTask);
                } else {
                    System.out.println("Invalid task ID or you don't have permission to view this task.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving tasks: " + e.getMessage());
            logger.error("Error retrieving tasks", e);
        }
    }
    
    /**
     * View available tasks for volunteers
     */
    private void viewAvailableTasks() {
        System.out.println("\n===== Available Tasks =====");
        
        try {
            List<Task> tasks = taskService.getAvailableTasks();
            
            if (tasks.isEmpty()) {
                System.out.println("There are no available tasks.");
                return;
            }
            
            for (Task task : tasks) {
                System.out.println("ID: " + task.getTaskId() + 
                        " | Title: " + task.getTitle() +
                        " | Date: " + task.getScheduledDate() +
                        " | Time: " + task.getScheduledTime() +
                        " | Urgency: " + task.getUrgencyLevel());
            }
            
            // Ask if the user wants to view details or accept a task
            System.out.println("\nEnter a task ID to view details and accept the task, or 0 to go back:");
            int taskId = readIntInput("Task ID: ", 0, Integer.MAX_VALUE);
            
            if (taskId > 0) {
                Task selectedTask = taskService.getTaskById(taskId);
                if (selectedTask != null && "AVAILABLE".equals(selectedTask.getStatus())) {
                    displayTaskDetails(selectedTask);
                    
                    // Ask if the volunteer wants to accept the task
                    System.out.println("\nDo you want to accept this task?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    int choice = readIntInput("Enter your choice: ", 1, 2);
                    
                    if (choice == 1) {
                        boolean success = taskService.assignTaskToVolunteer(taskId, currentUser.getUserId());
                        if (success) {
                            System.out.println("Task accepted successfully!");
                            logger.info("Volunteer {} accepted task {}", currentUser.getUserId(), taskId);
                        } else {
                            System.out.println("Failed to accept the task. It may have been assigned to another volunteer.");
                        }
                    }
                } else {
                    System.out.println("Invalid task ID or task is no longer available.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving tasks: " + e.getMessage());
            logger.error("Error retrieving tasks", e);
        }
    }
    
    /**
     * View tasks assigned to the current volunteer
     */
    private void viewMyAssignedTasks() {
        System.out.println("\n===== My Assigned Tasks =====");
        
        try {
            List<Task> tasks = taskService.getTasksByVolunteerId(currentUser.getUserId());
            
            if (tasks.isEmpty()) {
                System.out.println("You have no assigned tasks.");
                return;
            }
            
            for (Task task : tasks) {
                System.out.println("ID: " + task.getTaskId() + 
                        " | Title: " + task.getTitle() +
                        " | Status: " + task.getStatus() +
                        " | Date: " + task.getScheduledDate() +
                        " | Time: " + task.getScheduledTime());
            }
            
            // Ask if the user wants to view details of a specific task
            System.out.println("\nEnter a task ID to view details or 0 to go back:");
            int taskId = readIntInput("Task ID: ", 0, Integer.MAX_VALUE);
            
            if (taskId > 0) {
                Task selectedTask = taskService.getTaskById(taskId);
                if (selectedTask != null && selectedTask.getVolunteerId() == currentUser.getUserId()) {
                    displayTaskDetails(selectedTask);
                } else {
                    System.out.println("Invalid task ID or you don't have permission to view this task.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving tasks: " + e.getMessage());
            logger.error("Error retrieving tasks", e);
        }
    }
    
    /**
     * Update the status of a task assigned to the current volunteer
     */
    private void updateTaskStatus() {
        System.out.println("\n===== Update Task Status =====");
        
        try {
            List<Task> tasks = taskService.getTasksByVolunteerId(currentUser.getUserId());
            
            if (tasks.isEmpty()) {
                System.out.println("You have no assigned tasks to update.");
                return;
            }
            
            for (Task task : tasks) {
                System.out.println("ID: " + task.getTaskId() + 
                        " | Title: " + task.getTitle() +
                        " | Status: " + task.getStatus() +
                        " | Date: " + task.getScheduledDate() +
                        " | Time: " + task.getScheduledTime());
            }
            
            // Ask which task to update
            System.out.println("\nEnter a task ID to update status or 0 to go back:");
            int taskId = readIntInput("Task ID: ", 0, Integer.MAX_VALUE);
            
            if (taskId > 0) {
                Task selectedTask = taskService.getTaskById(taskId);
                if (selectedTask != null && selectedTask.getVolunteerId() != null && 
                        selectedTask.getVolunteerId() == currentUser.getUserId()) {
                    
                    // Display available status options based on current status
                    System.out.println("\nCurrent Status: " + selectedTask.getStatus());
                    System.out.println("Select new status:");
                    
                    if ("ASSIGNED".equals(selectedTask.getStatus())) {
                        System.out.println("1. IN_PROGRESS");
                        System.out.println("2. Cancel");
                        int choice = readIntInput("Enter your choice: ", 1, 2);
                        
                        if (choice == 1) {
                            boolean success = taskService.updateTaskStatus(taskId, "IN_PROGRESS");
                            if (success) {
                                System.out.println("Task status updated successfully!");
                                logger.info("Task {} status updated to IN_PROGRESS", taskId);
                            } else {
                                System.out.println("Failed to update task status.");
                            }
                        }
                    } else if ("IN_PROGRESS".equals(selectedTask.getStatus())) {
                        System.out.println("1. COMPLETED");
                        System.out.println("2. Cancel");
                        int choice = readIntInput("Enter your choice: ", 1, 2);
                        
                        if (choice == 1) {
                            boolean success = taskService.updateTaskStatus(taskId, "COMPLETED");
                            if (success) {
                                System.out.println("Task status updated successfully!");
                                logger.info("Task {} status updated to COMPLETED", taskId);
                            } else {
                                System.out.println("Failed to update task status.");
                            }
                        }
                    } else {
                        System.out.println("This task cannot be updated from its current status: " + 
                                selectedTask.getStatus());
                    }
                } else {
                    System.out.println("Invalid task ID or you don't have permission to update this task.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving tasks: " + e.getMessage());
            logger.error("Error retrieving tasks", e);
        }
    }
    
    /**
     * Update the current user's profile
     */
    private void updateProfile() {
        System.out.println("\n===== Update Profile =====");
        
        System.out.println("Current profile information:");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("First Name: " + currentUser.getFirstName());
        System.out.println("Last Name: " + currentUser.getLastName());
        System.out.println("Phone Number: " + (currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "Not set"));
        System.out.println("Address: " + (currentUser.getAddress() != null ? currentUser.getAddress() : "Not set"));
        System.out.println("Bio: " + (currentUser.getBio() != null ? currentUser.getBio() : "Not set"));
        
        System.out.println("\nLeave fields blank to keep current values.");
        
        String phoneNumber = readStringInputWithDefault("Phone Number: ", currentUser.getPhoneNumber());
        String address = readStringInputWithDefault("Address: ", currentUser.getAddress());
        String bio = readStringInputWithDefault("Bio: ", currentUser.getBio());
        
        try {
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setAddress(address);
            currentUser.setBio(bio);
            
            User updatedUser = userService.updateUserProfile(currentUser);
            
            System.out.println("\nProfile updated successfully!");
            logger.info("User {} profile updated", currentUser.getUserId());
            
            // Update the current user object
            currentUser = updatedUser;
        } catch (IllegalArgumentException e) {
            System.out.println("Profile update failed: " + e.getMessage());
            logger.warn("Profile update failed: {}", e.getMessage());
        } catch (SQLException e) {
            System.out.println("An error occurred during profile update: " + e.getMessage());
            logger.error("Profile update error", e);
        }
    }
    
    /**
     * Logout the current user
     */
    private void logout() {
        logger.info("User logged out: {}", currentUser.getUsername());
        currentUser = null;
        System.out.println("You have been logged out.");
    }
    
    /**
     * Display information about the application
     */
    private void displayAboutInfo() {
        System.out.println("\n===== About J.A.V.A (Just a Volunteer Away) =====");
        System.out.println("A community-driven platform that connects volunteers with elderly");
        System.out.println("individuals who need help with daily tasks.");
        System.out.println("Our mission: make volunteering more accessible and ensure elderly");
        System.out.println("users receive reliable support.");
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Display detailed information about a task
     * 
     * @param task the task to display
     */
    private void displayTaskDetails(Task task) {
        System.out.println("\n===== Task Details =====");
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Location: " + task.getLocation());
        System.out.println("Scheduled Date: " + task.getScheduledDate());
        System.out.println("Scheduled Time: " + task.getScheduledTime());
        System.out.println("Estimated Duration: " + task.getEstimatedDuration() + " minutes");
        System.out.println("Urgency Level: " + task.getUrgencyLevel());
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Read an integer input from the user within a specified range
     * 
     * @param prompt the prompt to display
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the validated integer input
     */
    private int readIntInput(String prompt, int min, int max) {
        int input;
        
        while (true) {
            System.out.print(prompt);
            
            try {
                input = Integer.parseInt(scanner.nextLine());
                
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Read a string input from the user
     * 
     * @param prompt the prompt to display
     * @return the input string
     */
    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    /**
     * Read a string input from the user with a default value
     * 
     * @param prompt the prompt to display
     * @param defaultValue the default value to use if the input is empty
     * @return the input string or the default value if the input is empty
     */
    private String readStringInputWithDefault(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        
        return input.trim().isEmpty() ? defaultValue : input;
    }
}