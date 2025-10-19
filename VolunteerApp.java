import java.util.List;
import java.util.Scanner;

/**
 * J.A.V.A (Just a Volunteer Away) - Main Application
 * A simple console-based volunteer management system
 */
public class VolunteerApp {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("\n================================================="); 
        System.out.println("  Welcome to J.A.V.A - Just a Volunteer Away");
        System.out.println("  Connecting volunteers with elderly in need");
        System.out.println("=================================================\n");
        
        // Test database connection
        if (!DatabaseManager.testConnection()) {
            System.err.println("ERROR: Cannot connect to database!");
            System.err.println("Please check:");
            System.err.println("1. MySQL is running");
            System.err.println("2. Database 'volunteer_app' exists");
            System.err.println("3. User credentials are correct in DatabaseManager.java");
            return;
        }
        
        System.out.println("Database connected successfully!\n");
        
        // Main application loop
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                if (currentUser.getRole().equals("ELDERLY")) {
                    showElderlyMenu();
                } else if (currentUser.getRole().equals("VOLUNTEER")) {
                    showVolunteerMenu();
                }
            }
        }
    }
    
    // ==================== LOGIN/REGISTRATION MENU ====================
    
    private static void showLoginMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("\nThank you for using J.A.V.A. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        currentUser = DatabaseManager.loginUser(username, password);
        
        if (currentUser != null) {
            System.out.println("\nWelcome back, " + currentUser.getFirstName() + "!");
        } else {
            System.out.println("\nInvalid username or password. Please try again.");
        }
    }
    
    private static void register() {
        System.out.println("\n--- Register New User ---");
        
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine().trim();
        
        System.out.println("\nSelect Role:");
        System.out.println("1. Elderly (need help)");
        System.out.println("2. Volunteer (provide help)");
        System.out.print("Choose: ");
        int roleChoice = getIntInput();
        
        String role = (roleChoice == 1) ? "ELDERLY" : "VOLUNTEER";
        
        User newUser = new User(username, password, email, firstName, lastName, phoneNumber, role);
        
        if (DatabaseManager.registerUser(newUser)) {
            System.out.println("\nRegistration successful! You can now log in.");
        } else {
            System.out.println("\nRegistration failed. Username or email might already exist.");
        }
    }
    
    // ==================== ELDERLY USER MENU ====================
    
    private static void showElderlyMenu() {
        System.out.println("\n=== Elderly User Menu ===");
        System.out.println("Logged in as: " + currentUser.getFirstName() + " " + currentUser.getLastName());
        System.out.println("\n1. Create New Task Request");
        System.out.println("2. View My Task Requests");
        System.out.println("3. Remove Volunteer from Task");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                createTask();
                break;
            case 2:
                viewMyTasks();
                break;
            case 3:
                reassignTask();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private static void createTask() {
        System.out.println("\n--- Create New Task ---");
        
        System.out.print("Task Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Location: ");
        String location = scanner.nextLine().trim();
        
        System.out.print("Scheduled Date (YYYY-MM-DD): ");
        String scheduledDate = scanner.nextLine().trim();
        
        System.out.print("Scheduled Time (HH:MM): ");
        String scheduledTime = scanner.nextLine().trim();
        
        System.out.print("Estimated Duration (minutes): ");
        int duration = getIntInput();
        
        Task task = new Task(title, description, currentUser.getUserId(), location, 
                            scheduledDate, scheduledTime, duration);
        
        if (DatabaseManager.createTask(task)) {
            System.out.println("\nTask created successfully! Task ID: " + task.getTaskId());
            System.out.println("Your task is now available for volunteers to accept.");
        } else {
            System.out.println("\nFailed to create task. Please try again.");
        }
    }
    
    private static void viewMyTasks() {
        System.out.println("\n--- My Task Requests ---");
        
        List<Task> tasks = DatabaseManager.getTasksByRequester(currentUser.getUserId());
        
        if (tasks.isEmpty()) {
            System.out.println("You haven't created any tasks yet.");
            return;
        }
        
        System.out.println("\nYour tasks:");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Task task : tasks) {
            System.out.println("ID: " + task.getTaskId() + " | " + task.getTitle());
            System.out.println("   Status: " + task.getStatus() + " | Date: " + task.getScheduledDate() + 
                             " at " + task.getScheduledTime());
            System.out.println("   Location: " + task.getLocation());
            if (task.getVolunteerId() != null) {
                System.out.println("   Assigned to Volunteer ID: " + task.getVolunteerId());
            }
            System.out.println("─────────────────────────────────────────────────────────────────");
        }
    }
    
    private static void reassignTask() {
        System.out.println("\n--- Remove Volunteer from Task ---");
        
        List<Task> tasks = DatabaseManager.getTasksByRequester(currentUser.getUserId());
        List<Task> assignedTasks = new java.util.ArrayList<>();
        
        // Filter for tasks with volunteers
        for (Task task : tasks) {
            if (task.getVolunteerId() != null && 
                (task.getStatus().equals("ASSIGNED") || task.getStatus().equals("IN_PROGRESS"))) {
                assignedTasks.add(task);
            }
        }
        
        if (assignedTasks.isEmpty()) {
            System.out.println("You don't have any tasks with assigned volunteers.");
            return;
        }
        
        System.out.println("\nTasks with volunteers:");
        for (Task task : assignedTasks) {
            System.out.println(task.getTaskId() + ". " + task.getTitle() + 
                             " (Volunteer ID: " + task.getVolunteerId() + ")");
        }
        
        System.out.print("\nEnter Task ID to remove volunteer (0 to cancel): ");
        int taskId = getIntInput();
        
        if (taskId == 0) {
            return;
        }
        
        if (DatabaseManager.reassignTask(taskId)) {
            System.out.println("\nVolunteer removed successfully!");
            System.out.println("Task is now available for other volunteers.");
        } else {
            System.out.println("\nFailed to remove volunteer. Please check the task ID.");
        }
    }
    
    // ==================== VOLUNTEER MENU ====================
    
    private static void showVolunteerMenu() {
        System.out.println("\n=== Volunteer Menu ===");
        System.out.println("Logged in as: " + currentUser.getFirstName() + " " + currentUser.getLastName());
        System.out.println("Points: " + currentUser.getPoints() + " | Tasks Completed: " + 
                          currentUser.getTasksCompleted());
        System.out.println("\n1. View Available Tasks");
        System.out.println("2. View My Assigned Tasks");
        System.out.println("3. Update Task Status");
        System.out.println("4. View Leaderboard");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
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
                viewLeaderboard();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private static void viewAvailableTasks() {
        System.out.println("\n--- Available Tasks ---");
        
        List<Task> tasks = DatabaseManager.getAvailableTasks();
        
        if (tasks.isEmpty()) {
            System.out.println("No available tasks at the moment.");
            return;
        }
        
        System.out.println("\nAvailable tasks:");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Task task : tasks) {
            System.out.println("ID: " + task.getTaskId() + " | " + task.getTitle());
            System.out.println("   Description: " + task.getDescription());
            System.out.println("   Date: " + task.getScheduledDate() + " at " + task.getScheduledTime());
            System.out.println("   Duration: " + task.getEstimatedDuration() + " minutes");
            System.out.println("   Location: " + task.getLocation());
            System.out.println("─────────────────────────────────────────────────────────────────");
        }
        
        System.out.print("\nEnter Task ID to accept (0 to cancel): ");
        int taskId = getIntInput();
        
        if (taskId == 0) {
            return;
        }
        
        if (DatabaseManager.assignTask(taskId, currentUser.getUserId())) {
            System.out.println("\nTask accepted successfully!");
            System.out.println("You can now see it in 'My Assigned Tasks'.");
        } else {
            System.out.println("\nFailed to accept task. It may have been already assigned.");
        }
    }
    
    private static void viewMyAssignedTasks() {
        System.out.println("\n--- My Assigned Tasks ---");
        
        List<Task> tasks = DatabaseManager.getTasksByVolunteer(currentUser.getUserId());
        
        if (tasks.isEmpty()) {
            System.out.println("You don't have any assigned tasks yet.");
            return;
        }
        
        System.out.println("\nYour tasks:");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Task task : tasks) {
            System.out.println("ID: " + task.getTaskId() + " | " + task.getTitle());
            System.out.println("   Status: " + task.getStatus());
            System.out.println("   Date: " + task.getScheduledDate() + " at " + task.getScheduledTime());
            System.out.println("   Duration: " + task.getEstimatedDuration() + " minutes");
            System.out.println("   Location: " + task.getLocation());
            System.out.println("─────────────────────────────────────────────────────────────────");
        }
    }
    
    private static void updateTaskStatus() {
        System.out.println("\n--- Update Task Status ---");
        
        List<Task> tasks = DatabaseManager.getTasksByVolunteer(currentUser.getUserId());
        
        if (tasks.isEmpty()) {
            System.out.println("You don't have any assigned tasks.");
            return;
        }
        
        System.out.println("\nYour tasks:");
        for (Task task : tasks) {
            System.out.println(task.getTaskId() + ". " + task.getTitle() + " (Status: " + 
                             task.getStatus() + ")");
        }
        
        System.out.print("\nEnter Task ID to update (0 to cancel): ");
        int taskId = getIntInput();
        
        if (taskId == 0) {
            return;
        }
        
        System.out.println("\nSelect new status:");
        System.out.println("1. IN_PROGRESS");
        System.out.println("2. COMPLETED");
        System.out.print("Choose: ");
        int statusChoice = getIntInput();
        
        String status = (statusChoice == 1) ? "IN_PROGRESS" : "COMPLETED";
        
        if (DatabaseManager.updateTaskStatus(taskId, status)) {
            System.out.println("\nTask status updated successfully!");
            if (status.equals("COMPLETED")) {
                System.out.println("Points have been added to your account!");
            }
        } else {
            System.out.println("\nFailed to update task status.");
        }
    }
    
    private static void viewLeaderboard() {
        System.out.println("\n--- Volunteer Leaderboard ---");
        
        List<User> leaderboard = DatabaseManager.getLeaderboard(10);
        
        if (leaderboard.isEmpty()) {
            System.out.println("No volunteers yet!");
            return;
        }
        
        System.out.println("\nTop 10 Volunteers:");
        System.out.println("=================================================");
        System.out.printf("%-5s %-20s %-10s %-15s%n", "Rank", "Name", "Points", "Tasks Done");
        System.out.println("=================================================");
        
        int rank = 1;
        for (User user : leaderboard) {
            String name = user.getFirstName() + " " + user.getLastName();
            System.out.printf("%-5d %-20s %-10d %-15d%n", 
                rank++, name, user.getPoints(), user.getTasksCompleted());
        }
        System.out.println("=================================================");
    }
    
    // ==================== UTILITY METHODS ====================
    
    private static void logout() {
        System.out.println("\nLogging out... Goodbye, " + currentUser.getFirstName() + "!");
        currentUser = null;
    }
    
    private static int getIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
