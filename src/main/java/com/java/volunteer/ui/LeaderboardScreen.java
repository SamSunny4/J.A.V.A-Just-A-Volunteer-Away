package com.java.volunteer.ui;

import java.util.List;

import com.java.volunteer.model.User;
import com.java.volunteer.model.UserPoints;
import com.java.volunteer.service.UserPointsService;

/**
 * Screen for displaying volunteer leaderboard
 */
public class LeaderboardScreen extends BaseScreen {
    private final UserPointsService userPointsService;
    
    public LeaderboardScreen(User currentUser, UserPointsService userPointsService) {
        super(currentUser);
        this.userPointsService = userPointsService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("Volunteer Leaderboard");
        
        List<UserPoints> leaderboard = userPointsService.getLeaderboard(10); // Get top 10
        
        if (leaderboard.isEmpty()) {
            System.out.println("\nNo volunteer points recorded yet.");
        } else {
            displayLeaderboard(leaderboard);
        }
        
        int choice = showMenu(
            "View Full Leaderboard",
            "Back to Main Menu"
        );
        
        switch (choice) {
            case 1:
                return showFullLeaderboard();
            case 2:
                return new MainMenuScreen(currentUser, null, null, userPointsService);
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private void displayLeaderboard(List<UserPoints> leaderboard) {
        System.out.println("\n===== Top Volunteers =====");
        System.out.println(String.format("%-5s %-20s %-10s %-15s", "Rank", "Name", "Points", "Tasks Completed"));
        System.out.println("-----------------------------------------------");
        
        int rank = 1;
        for (UserPoints points : leaderboard) {
            System.out.println(String.format("%-5d %-20s %-10d %-15d", 
                rank++, 
                points.getUser().getFullName(), 
                points.getPoints(), 
                points.getTasksCompleted()));
        }
    }
    
    private BaseScreen showFullLeaderboard() {
        showHeader("Full Volunteer Leaderboard");
        
        List<UserPoints> fullLeaderboard = userPointsService.getLeaderboard(0); // Get all entries
        
        if (fullLeaderboard.isEmpty()) {
            System.out.println("\nNo volunteer points recorded yet.");
        } else {
            displayLeaderboard(fullLeaderboard);
        }
        
        showMessage("Press Enter to return to leaderboard menu...");
        return this;
    }
}