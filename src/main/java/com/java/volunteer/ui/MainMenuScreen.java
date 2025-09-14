package com.java.volunteer.ui;

import com.java.volunteer.model.User;
import com.java.volunteer.model.UserRole;
import com.java.volunteer.service.TaskService;
import com.java.volunteer.service.UserPointsService;
import com.java.volunteer.service.UserService;

/**
 * Main menu screen that displays options based on user role
 */
public class MainMenuScreen extends BaseScreen {
    private final UserService userService;
    private final TaskService taskService;
    private final UserPointsService userPointsService;
    
    public MainMenuScreen(User currentUser, UserService userService, 
                         TaskService taskService, UserPointsService userPointsService) {
        super(currentUser);
        this.userService = userService;
        this.taskService = taskService;
        this.userPointsService = userPointsService;
    }

    @Override
    public BaseScreen display() {
        showHeader("Main Menu");
        
        if (currentUser.getRoleId() == UserRole.ROLE_ELDERLY) {
            return displayElderlyMenu();
        } else if (currentUser.getRoleId() == UserRole.ROLE_VOLUNTEER) {
            return displayVolunteerMenu();
        } else if (currentUser.getRoleId() == UserRole.ROLE_ADMIN) {
            return displayAdminMenu();
        } else {
            System.out.println("\nUnknown user role: " + currentUser.getRoleId());
            return null;
        }
    }
    
    private BaseScreen displayElderlyMenu() {
        int choice = showMenu(
            "My Profile",
            "Create Task Request",
            "My Task Requests",
            "Manage Task Volunteers",
            "Logout"
        );
        
        switch (choice) {
            case 1:
                return new ProfileScreen(currentUser, userService);
            case 2:
                return new CreateTaskScreen(currentUser, taskService);
            case 3:
                return new MyTasksScreen(currentUser, taskService);
            case 4:
                return new ManageVolunteersScreen(currentUser, taskService, userPointsService);
            case 5:
                showMessage("You have been logged out.");
                return new LoginScreen(userService);
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private BaseScreen displayVolunteerMenu() {
        int choice = showMenu(
            "My Profile",
            "Available Tasks",
            "My Assigned Tasks",
            "Leaderboard",
            "Logout"
        );
        
        switch (choice) {
            case 1:
                return new ProfileScreen(currentUser, userService);
            case 2:
                return new AvailableTasksScreen(currentUser, taskService);
            case 3:
                return new MyAssignedTasksScreen(currentUser, taskService);
            case 4:
                return new LeaderboardScreen(currentUser, userPointsService);
            case 5:
                showMessage("You have been logged out.");
                return new LoginScreen(userService);
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private BaseScreen displayAdminMenu() {
        int choice = showMenu(
            "My Profile",
            "Manage Users",
            "Manage Tasks",
            "View Leaderboard",
            "System Reports",
            "Logout"
        );
        
        switch (choice) {
            case 1:
                return new ProfileScreen(currentUser, userService);
            case 2:
                System.out.println("Manage Users screen not yet implemented");
                return this; // Placeholder until ManageUsersScreen is implemented
            case 3:
                System.out.println("Manage Tasks screen not yet implemented");
                return this; // Placeholder until ManageTasksScreen is implemented
            case 4:
                return new LeaderboardScreen(currentUser, userPointsService);
            case 5:
                System.out.println("Reports screen not yet implemented");
                return this; // Placeholder until ReportsScreen is implemented
            case 6:
                showMessage("You have been logged out.");
                return new LoginScreen(userService);
            case 0:
                return null;
            default:
                return this;
        }
    }
}