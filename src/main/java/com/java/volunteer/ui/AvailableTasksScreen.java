package com.java.volunteer.ui;

import java.util.List;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.TaskHistory;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

/**
 * Screen for volunteers to view and accept available tasks
 */
public class AvailableTasksScreen extends BaseScreen {
    private final TaskService taskService;
    
    public AvailableTasksScreen(User currentUser, TaskService taskService) {
        super(currentUser);
        this.taskService = taskService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("Available Tasks");
        
        List<Task> availableTasks = taskService.getAvailableTasks();
        
        if (availableTasks.isEmpty()) {
            showMessage("There are no available tasks at the moment. Please check back later.");
            return new MainMenuScreen(currentUser, null, taskService, null);
        }
        
        displayTasks(availableTasks);
        
        int choice = showMenu(
            "View Task Details and Accept",
            "Back to Main Menu"
        );
        
        switch (choice) {
            case 1:
                return viewAndAcceptTask(availableTasks);
            case 2:
                return new MainMenuScreen(currentUser, null, taskService, null);
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private void displayTasks(List<Task> tasks) {
        System.out.println("\nAvailable tasks:");
        System.out.println(String.format("%-5s %-30s %-15s %-15s %-10s", 
            "ID", "Title", "Date", "Location", "Urgency"));
        System.out.println("--------------------------------------------------------------------------------");
        
        for (Task task : tasks) {
            System.out.println(String.format("%-5d %-30s %-15s %-15s %-10s",
                task.getTaskId(),
                truncateString(task.getTitle(), 28),
                task.getScheduledDate().toString(),
                truncateString(task.getLocation(), 13),
                task.getUrgencyLevel()));
        }
    }
    
    private BaseScreen viewAndAcceptTask(List<Task> tasks) {
        int taskId = getNumericInput("\nEnter task ID to view details (0 to go back)");
        if (taskId == 0) {
            return this;
        }
        
        Task selectedTask = tasks.stream()
            .filter(task -> task.getTaskId() == taskId)
            .findFirst()
            .orElse(null);
        
        if (selectedTask == null) {
            showMessage("Invalid task ID selected.");
            return this;
        }
        
        showHeader("Task Details");
        
        System.out.println("\nTitle: " + selectedTask.getTitle());
        System.out.println("Description: " + selectedTask.getDescription());
        System.out.println("Location: " + selectedTask.getLocation());
        System.out.println("Date & Time: " + selectedTask.getScheduledDate() + " " + selectedTask.getScheduledTime());
        System.out.println("Duration: " + selectedTask.getEstimatedDuration() + " minutes");
        System.out.println("Urgency: " + selectedTask.getUrgencyLevel());
        
        if (selectedTask.getRequester() != null) {
            System.out.println("\nRequested by: " + selectedTask.getRequester().getFullName());
        }
        
        if (confirm("\nWould you like to accept this task?")) {
            Task updatedTask = taskService.assignTask(
                selectedTask.getTaskId(), 
                currentUser.getUserId(),
                currentUser.getUserId(),
                TaskHistory.ChangedBy.VOLUNTEER
            );
            
            if (updatedTask != null) {
                showMessage("You have successfully accepted the task!");
                return new MyAssignedTasksScreen(currentUser, taskService);
            } else {
                showMessage("Failed to accept the task. It may have been taken by another volunteer.");
            }
        }
        
        return this;
    }
    
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}